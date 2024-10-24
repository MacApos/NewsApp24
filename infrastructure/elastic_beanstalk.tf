locals {
  web_sg_name = "${var.app_name}-env-sg"
}

resource "aws_security_group" "eb_web_sg" {
  name   = local.web_sg_name
  vpc_id = module.vpc.vpc_id
  tags = {
    Name = local.web_sg_name
  }
}

# check if can be deleted
resource "aws_vpc_security_group_ingress_rule" "eb_web_sg_allow_all_ssh" {
  security_group_id = aws_security_group.eb_web_sg.id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1"
}

resource "aws_vpc_security_group_ingress_rule" "eb_web_sg_allow_all_tcp" {
  security_group_id = aws_security_group.eb_web_sg.id
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 22
  ip_protocol       = "tcp"
  to_port           = 22
}

resource "aws_vpc_security_group_egress_rule" "eb_web_sg_allow_all_traffic" {
  security_group_id = aws_security_group.eb_web_sg.id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1"
}

resource "aws_vpc_security_group_ingress_rule" "eb_web_sg_db" {
  security_group_id            = aws_default_security_group.default_sg.id
  referenced_security_group_id = aws_security_group.eb_web_sg.id
  from_port                    = 3306
  ip_protocol                  = "tcp"
  to_port                      = 3306
}

# resource "aws_iam_role" "eb_service_role" {
#   name = "AWSElasticBeanstalkServiceRole"
#   assume_role_policy = jsonencode({
#     Version = "2012-10-17",
#     Statement = [
#       {
#         Action = "sts:AssumeRole",
#         Effect = "Allow",
#         Principal = {
#           Service = "ec2.amazonaws.com"
#         }
#       }
#     ]
#   })
# }
#
# resource "aws_iam_role" "eb_ec2_role" {
#   name = "AWSElasticBeanstalkEC2Role"
#   assume_role_policy = jsonencode({
#     Version = "2012-10-17",
#     Statement = [
#       {
#         Action = "sts:AssumeRole",
#         Effect = "Allow",
#         Principal = {
#           Service = "ec2.amazonaws.com"
#         }
#       }
#     ]
#   })
# }
#
# resource "aws_iam_role_policy_attachment" "eb_service_role_attachment" {
#   for_each = toset([
#     "arn:aws:iam::aws:policy/service-role/AWSElasticBeanstalkEnhancedHealth",
#     "arn:aws:iam::aws:policy/AWSElasticBeanstalkManagedUpdatesCustomerRolePolicy",
#     "arn:aws:iam::aws:policy/SecretsManagerReadWrite"
#   ])
#   role       = aws_iam_role.eb_service_role.name
#   policy_arn = each.key
# }
#
# resource "aws_iam_role_policy_attachment" "eb_ec2_role_attachment" {
#   for_each = toset([
#     "arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier",
#     "arn:aws:iam::aws:policy/AWSElasticBeanstalkWorkerTier",
#     "arn:aws:iam::aws:policy/AWSElasticBeanstalkMulticontainerDocker"
#   ])
#   role       = aws_iam_role.eb_ec2_role.name
#   policy_arn = each.key
# }

resource "aws_iam_instance_profile" "eb_instance_profile" {
  #   name = "AWSElasticBeanstalkEC2Role"
  name = aws_iam_role.eb_ec2_role.name
  role = aws_iam_role.eb_service_role.name
}

# resource "random_id" "random_id" {
#   byte_length = 8
# }
#
# resource "aws_s3_bucket" "app_bucket" {
#   bucket = "${lower(var.app_name)}s3${random_id.random_id.hex}"
# }

# resource "aws_s3_object" "eb_bucket_object" {
#   bucket = aws_s3_bucket.app_bucket.bucket
#   key    = "elastic-beanstalk.zip"
#   source = "../software/ElasticBeanstalk/build/libs/elastic-beanstalk.zip"
# }

# resource "aws_elastic_beanstalk_application_version" "app_web_version" {
#   name        = "${var.app_name}WebVersion"
#   application = aws_elastic_beanstalk_application.app_web.name
#   bucket      = aws_s3_bucket.app_bucket.bucket
#   key         = aws_s3_object.eb_bucket_object.key
# }

resource "aws_elastic_beanstalk_application" "app_web" {
  name = "${var.app_name}WebApp"
}

resource "aws_elastic_beanstalk_environment" "app_web_env" {
  #   count = length(local.environments)
  #   name = local.environments[count.index]["name"]
  #   tier = local.environments[count.index]["tier"]

  name                = "${var.app_name}WebEnv"
  application         = aws_elastic_beanstalk_application.app_web.name
  solution_stack_name = "64bit Amazon Linux 2023 v4.2.7 running Corretto 17"
  #   version_label       = aws_elastic_beanstalk_application_version.app_web_version.name

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "ServiceRole"
    value     = aws_iam_role.eb_service_role.arn
  }

  setting {
    namespace = "aws:elasticbeanstalk:managedactions"
    name      = "ServiceRoleForManagedUpdates"
    value     = aws_iam_role.eb_service_role.arn
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment:process:default"
    name      = "HealthCheckPath"
    value     = "/docs"
  }

  setting {
    namespace = "aws:elasticbeanstalk:environment:process:default"
    name      = "MatcherHTTPCode"
    value     = "200"
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = aws_iam_instance_profile.eb_instance_profile.name
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "SecurityGroups"
    value     = aws_security_group.eb_web_sg.id
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "VPCId"
    value     = module.vpc.vpc_id
  }

  setting {
    namespace = "aws:ec2:vpc"
    name      = "Subnets"
    value = join(",", module.vpc.public_subnets)
  }
}
