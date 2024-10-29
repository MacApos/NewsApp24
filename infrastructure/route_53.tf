# locals {
#   path = "../software/StaticSite/build"
# }
#
# resource "aws_s3_bucket" "domain_bucket" {
#   bucket = var.root_domain
# }
#
# module "template_files" {
#   source   = "hashicorp/dir/template"
#   base_dir = "../software/StaticSite/build"
# }
#
# resource "aws_s3_object" "domain_bucket_object" {
#   for_each = module.template_files.files
#   bucket   = aws_s3_bucket.domain_bucket.bucket
#   content_type = each.value.content_type
#   source  = each.value.source_path
#   key      = each.key
# }
#
# resource "aws_s3_bucket_public_access_block" "domain_bucket_access_block" {
#   bucket                  = aws_s3_bucket.domain_bucket.id
#   block_public_acls       = false
#   block_public_policy     = false
#   ignore_public_acls      = false
#   restrict_public_buckets = false
# }
#
# resource "aws_s3_bucket_website_configuration" "domain_bucket_website_configuration" {
#   bucket = aws_s3_bucket.domain_bucket.bucket
#   index_document {
#     suffix = "index.html"
#   }
# }
#
# resource "aws_route53_record" "domain_bucket_record" {
#   name    = ""
#   type    = "A"
#   zone_id = var.zone_id
#   alias {
#     evaluate_target_health = false
#     name = trimprefix(aws_s3_bucket_website_configuration.domain_bucket_website_configuration.website_endpoint,
#       "${var.root_domain}.")
#     zone_id = aws_s3_bucket.domain_bucket.hosted_zone_id
#   }
# }
#
# resource "aws_s3_bucket" "subdomain_bucket" {
#   bucket = "www.${var.root_domain}"
# }
#
# resource "aws_s3_bucket_website_configuration" "subdomain_bucket_website_configuration" {
#   bucket = aws_s3_bucket.subdomain_bucket.bucket
#   redirect_all_requests_to {
#     host_name = aws_s3_bucket.domain_bucket.id
#     protocol  = "http"
#   }
# }
#
# resource "aws_route53_record" "subdomain_bucket_record" {
#   name    = "www"
#   type    = "A"
#   zone_id = var.zone_id
#   alias {
#     evaluate_target_health = false
#     name = trimprefix(aws_s3_bucket_website_configuration.subdomain_bucket_website_configuration.website_endpoint,
#       "${var.root_domain}.")
#     zone_id = aws_s3_bucket.subdomain_bucket.hosted_zone_id
#   }
# }
#
# resource "aws_s3_bucket_policy" "allow_public_read" {
#   bucket = aws_s3_bucket.domain_bucket.id
#   policy = jsonencode({
#     Version = "2012-10-17",
#     Statement = [
#       {
#         Sid       = "PublicReadGetObject",
#         Effect    = "Allow",
#         Principal = "*",
#         Action = ["s3:GetObject"],
#         Resource  = "${aws_s3_bucket.domain_bucket.arn}/*"
#       }
#     ]
#   })
# }