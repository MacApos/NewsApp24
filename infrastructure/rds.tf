locals {
#   subnet_ids = flatten([module.vpc.public_subnets, module.vpc.private_subnets])
  v1 = {
    username = "admin"
#     password = data.aws_secretsmanager_random_password.password.random_password
        password = "password"
  }
}

data "aws_secretsmanager_random_password" "password" {
  password_length     = 20
  exclude_numbers     = false
  exclude_punctuation = true
  include_space       = false
}

resource "aws_secretsmanager_secret" "app_secret" {
  name = "prod/${var.app_name}/db_credentials"
}

resource "aws_secretsmanager_secret_version" "app_secret_version" {
  secret_id = aws_secretsmanager_secret.app_secret.id
  secret_string = jsonencode(local.v1)
}
#
# resource "aws_db_subnet_group" "vpc_subnet_group" {
#   name       = "${lower(var.app_name)}-subnet-group"
#   subnet_ids = local.subnet_ids
# }
#
# resource "aws_db_instance" "app_db" {
#   identifier           = "${lower(var.app_name)}-db"
#   instance_class       = "db.t3.micro"
#   allocated_storage    = 10
#   db_name              = "news"
#   engine               = "mysql"
#   engine_version       = "8.0.35"
#   username             = local.v1.username
#   password             = local.v1.password
#   db_subnet_group_name = aws_db_subnet_group.vpc_subnet_group.name
#   vpc_security_group_ids = [
#     aws_default_security_group.default_sg.id,
#   ]
#   publicly_accessible = true
#   skip_final_snapshot = true
# }
#
# locals {
#   v2 = merge(local.v1, {
#     engine               = aws_db_instance.app_db.engine
#     host                 = aws_db_instance.app_db.address
#     port                 = aws_db_instance.app_db.port
#     dbInstanceIdentifier = aws_db_instance.app_db.identifier
#   })
# }
#
# resource "aws_secretsmanager_secret_version" "app_secret_version2" {
#   secret_id = aws_secretsmanager_secret.app_secret.id
#   secret_string = jsonencode(local.v2)
# }