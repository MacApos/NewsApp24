# locals {
#   worker_sg_name = "${var.app_name}-worker-env-sg"
# }
#
# resource "aws_security_group" "eb_worker_sg" {
#   name   = local.worker_sg_name
#   vpc_id = module.vpc.vpc_id
#   tags = {
#     Name = local.worker_sg_name
#   }
# }
#
# # check if can be deleted
# resource "aws_vpc_security_group_ingress_rule" "eb_worker_sg_allow_all_ssh" {
#   security_group_id = aws_security_group.eb_worker_sg.id
#   cidr_ipv4         = "0.0.0.0/0"
#   ip_protocol       = "-1"
# }
#
# resource "aws_vpc_security_group_ingress_rule" "eb_worker_sg_allow_all_tcp" {
#   security_group_id = aws_security_group.eb_worker_sg.id
#   cidr_ipv4         = "0.0.0.0/0"
#   from_port         = 22
#   ip_protocol       = "tcp"
#   to_port           = 22
# }
#
# resource "aws_vpc_security_group_egress_rule" "eb_worker_sg_allow_all_traffic" {
#   security_group_id = aws_security_group.eb_worker_sg.id
#   cidr_ipv4         = "0.0.0.0/0"
#   ip_protocol       = "-1"
# }
#
# resource "aws_vpc_security_group_ingress_rule" "eb_worker_sg_db" {
#   security_group_id            = aws_default_security_group.default_sg.id
#   referenced_security_group_id = aws_security_group.eb_worker_sg.id
#   from_port                    = 3306
#   ip_protocol                  = "tcp"
#   to_port                      = 3306
# }
#
# # resource "aws_elastic_beanstalk_application_version" "app_worker_version" {
# #   name        = "${var.app_name}WorkerVersion"
# #   application = aws_elastic_beanstalk_application.app_worker.name
# #   bucket      = aws_s3_bucket.app_bucket.bucket
# #   key         = aws_s3_object.eb_bucket_object.key
# # }
#
# resource "aws_elastic_beanstalk_application" "app_worker" {
#   name = "${var.app_name}WorkerApp"
# }
#
# resource "aws_elastic_beanstalk_environment" "app_worker_env" {
#   name                = "${var.app_name}WorkerEnv"
#   application         = aws_elastic_beanstalk_application.app_worker.name
#   solution_stack_name = "64bit Amazon Linux 2023 v4.2.7 running Corretto 17"
#   tier                = "Worker"
# #   version_label       = aws_elastic_beanstalk_application_version.app_worker_version.name
#
#   setting {
#     namespace = "aws:autoscaling:launchconfiguration"
#     name      = "IamInstanceProfile"
#     value     = aws_iam_instance_profile.eb_instance_profile.name
#   }
#
#   setting {
#     namespace = "aws:autoscaling:launchconfiguration"
#     name      = "SecurityGroups"
#     value     = aws_security_group.eb_worker_sg.id
#   }
#
#   setting {
#     namespace = "aws:ec2:vpc"
#     name      = "VPCId"
#     value     = module.vpc.vpc_id
#   }
#
#   setting {
#     namespace = "aws:ec2:vpc"
#     name      = "Subnets"
#     value = join(",", module.vpc.public_subnets)
#   }
# }
