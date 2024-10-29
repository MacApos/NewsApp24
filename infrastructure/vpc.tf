# provider "aws" {
#   region = var.region
# }
#
# data "aws_availability_zones" "available" {}
#
# module "vpc" {
#   source                  = "terraform-aws-modules/vpc/aws"
#   version                 = "5.13.0"
#   name                    = "${var.app_name}-vpc"
#   cidr                    = "10.0.0.0/16"
#   azs                     = data.aws_availability_zones.available.names
#   public_subnets = ["10.0.0.0/20", "10.0.16.0/20"]
#   private_subnets = ["10.0.128.0/20", "10.0.144.0/20"]
#   enable_dns_support      = true
#   enable_dns_hostnames    = true
#   map_public_ip_on_launch = true
# }
#
# resource "aws_default_security_group" "default_sg" {
#   vpc_id = module.vpc.vpc_id
#   tags = {
#     Name = "${module.vpc.name}-default"
#   }
# }
#
# resource "aws_vpc_security_group_ingress_rule" "all_traffic" {
#   security_group_id            = aws_default_security_group.default_sg.id
#   referenced_security_group_id = aws_default_security_group.default_sg.id
#   ip_protocol                  = "-1"
# }
#
# data "http" "my_ip" {
#   url = "https://icanhazip.com"
# }
#
# resource "aws_vpc_security_group_ingress_rule" "default_inbound_rule" {
#   security_group_id = aws_default_security_group.default_sg.id
#   cidr_ipv4         = "0.0.0.0/0"
#   from_port         = 3306
#   ip_protocol       = "tcp"
#   to_port           = 3306
# }
#
# resource "aws_vpc_security_group_egress_rule" "default_outbound_rule" {
#   security_group_id = aws_default_security_group.default_sg.id
#   cidr_ipv4         = "0.0.0.0/0"
#   ip_protocol       = "-1"
# }