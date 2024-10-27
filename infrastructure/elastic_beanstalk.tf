locals {
  web_sg_name = "${var.app_name}-env-sg"
  web_env = {
    name                = "${var.app_name}WebEnv"
    application_name    = "${var.app_name}WebApp"
    tier                = "WebServer"
    version_label       = ""
    security_group_name = "${var.app_name}-web-env-sg"
    instance_type       = "t3.micro"
  }
  worker_env = {
    name                = "${var.app_name}WorkerEnv"
    application_name    = "${var.app_name}WorkerApp"
    tier                = "Worker"
    version_label       = ""
    security_group_name = "${var.app_name}-worker-env-sg"
    instance_type       = "t3.small"
  }
  environments = tolist([local.web_env, local.worker_env])
}

resource "aws_iam_role" "eb_service_role" {
  name = "AWSElasticBeanstalkServiceRole"
  path = "/service-role/"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "elasticbeanstalk.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role" "eb_ec2_role" {
  name = "AWSElasticBeanstalkEC2Role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "eb_service_role_attachment" {
  for_each = toset([
    "arn:aws:iam::aws:policy/service-role/AWSElasticBeanstalkEnhancedHealth",
    "arn:aws:iam::aws:policy/AWSElasticBeanstalkManagedUpdatesCustomerRolePolicy",
  ])
  role       = aws_iam_role.eb_service_role.name
  policy_arn = each.key
}

resource "aws_iam_role_policy_attachment" "eb_ec2_role_attachment" {
  for_each = toset([
    "arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier",
    "arn:aws:iam::aws:policy/AWSElasticBeanstalkWorkerTier",
    "arn:aws:iam::aws:policy/AWSElasticBeanstalkMulticontainerDocker",
    "arn:aws:iam::aws:policy/SecretsManagerReadWrite",
    "arn:aws:iam::aws:policy/AmazonS3FullAccess"
  ])
  role       = aws_iam_role.eb_ec2_role.name
  policy_arn = each.key
}

resource "aws_iam_instance_profile" "eb_instance_profile" {
  name = aws_iam_role.eb_ec2_role.name
  role = aws_iam_role.eb_ec2_role.name
}

resource "aws_security_group" "eb_security_groups" {
  count = length(local.environments)
  name   = local.environments[count.index]["security_group_name"]
  vpc_id = module.vpc.vpc_id
  tags = {
    Name = local.environments[count.index]["security_group_name"]
  }
}

# check if can be deleted
resource "aws_vpc_security_group_ingress_rule" "eb_web_sg_allow_all_ssh" {
  count = length(local.environments)
  security_group_id = aws_security_group.eb_security_groups[count.index].id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1"
}

resource "aws_vpc_security_group_ingress_rule" "eb_web_sg_allow_all_tcp" {
  count = length(local.environments)
  security_group_id = aws_security_group.eb_security_groups[count.index].id
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 22
  ip_protocol       = "tcp"
  to_port           = 22
}

resource "aws_vpc_security_group_egress_rule" "eb_web_sg_allow_all_traffic" {
  count = length(local.environments)
  security_group_id = aws_security_group.eb_security_groups[count.index].id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1"
}

resource "aws_vpc_security_group_ingress_rule" "eb_web_sg_db" {
  count = length(local.environments)
  security_group_id            = aws_default_security_group.default_sg.id
  referenced_security_group_id = aws_security_group.eb_security_groups[count.index].id
  from_port                    = 3306
  ip_protocol                  = "tcp"
  to_port                      = 3306
}

resource "random_id" "random_id" {
  byte_length = 8
}

# resource "aws_s3_bucket" "app_bucket" {
#   bucket = "${lower(var.app_name)}s3${random_id.random_id.hex}"
# }
#
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
#
resource "aws_elastic_beanstalk_application" "applications" {
  count = length(local.environments)
  name = local.environments[count.index]["application_name"]
}

resource "aws_elastic_beanstalk_environment" "app_web_env" {
  count = length(local.environments)
  name                = local.environments[count.index]["name"]
  tier                = local.environments[count.index]["tier"]
  application         = aws_elastic_beanstalk_application.applications[count.index].name
  solution_stack_name = "64bit Amazon Linux 2023 v4.2.7 running Corretto 17"
  #   version_label       = aws_elastic_beanstalk_application_version.app_web_version.name

  setting {
    namespace = "aws:elasticbeanstalk:environment"
    name      = "ServiceRole"
    value     = aws_iam_role.eb_service_role.arn
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "IamInstanceProfile"
    value     = aws_iam_instance_profile.eb_instance_profile.name
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "InstanceType"
    value     = local.environments[count.index]["instance_type"]
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name      = "SecurityGroups"
    value     = aws_security_group.eb_security_groups[count.index].id
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
