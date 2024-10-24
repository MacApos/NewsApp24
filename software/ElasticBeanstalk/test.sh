declare -A service_role
service_role["name"]="aws-elasticbeanstalk-service-role"
service_role["policies"]="
arn:aws:iam::aws:policy/service-role/AWSElasticBeanstalkEnhancedHealth
arn:aws:iam::aws:policy/AWSElasticBeanstalkManagedUpdatesCustomerRolePolicy
arn:aws:iam::aws:policy/SecretsManagerReadWrite"

declare -A ec2_role
ec2_role["name"]="aws-elasticbeanstalk-ec2-role"
ec2_role["policies"]="
arn:aws:iam::aws:policy/AWSElasticBeanstalkWebTier
arn:aws:iam::aws:policy/AWSElasticBeanstalkWorkerTier
arn:aws:iam::aws:policy/AWSElasticBeanstalkMulticontainerDocker"

declare -A roles
roles[0]=service_role
roles[1]=ec2_role

echo "Deleting instance profile"
aws iam remove-role-from-instance-profile \
  --instance-profile-name "${ec2_role["name"]}" \
  --role-name "${service_role["name"]}"
aws iam delete-instance-profile --instance-profile-name "${ec2_role["name"]}"

for role_key in 0 1
do
  eval "role_name=\${${roles[$role_key]}[name]}"
  eval "policies=\${${roles[$role_key]}[policies]}"

  echo "Detaching policies from $role_name"
  for policy in ${policies}
  do
    aws iam detach-role-policy --role-name "$role_name" --policy-arn "$policy"
  done
  echo "Deleting policies from $role_name"
  aws iam delete-role --role-name "$role_name"
done

for role_key in 0 1
do
  eval "role_name=\${${roles[$role_key]}[name]}"
  eval "policies=\${${roles[$role_key]}[policies]}"

  echo "Creating $role_name"
  aws iam create-role \
    --role-name "$role_name" \
    --assume-role-policy-document '{
      "Version": "2012-10-17",
      "Statement": [
          {
              "Action": "sts:AssumeRole",
              "Effect": "Allow",
              "Principal": {
                  "Service": "ec2.amazonaws.com"
              }
          }
      ]
  }'

  echo "Attaching policies to $role_name"
  for policy in ${policies}
  do
    aws iam attach-role-policy --role-name "$role_name" --policy-arn "$policy"
  done
done

aws iam create-instance-profile --instance-profile-name "${ec2_role["name"]}"
aws iam add-role-to-instance-profile \
  --role-name "${service_role["name"]}" \
  --instance-profile-name "${ec2_role["name"]}"