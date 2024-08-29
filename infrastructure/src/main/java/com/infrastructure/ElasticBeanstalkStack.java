package com.infrastructure;

import software.amazon.awscdk.services.elasticbeanstalk.CfnApplication;
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplicationVersion;
import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironment;
import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironment.OptionSettingProperty;
import software.amazon.awscdk.services.iam.CfnInstanceProfile;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.s3.assets.Asset;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.List;

import static com.infrastructure.InfrastructureApp.*;

public class ElasticBeanstalkStack extends Stack {
    public ElasticBeanstalkStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Create S3 asset from jar file
        Asset asset = Asset.Builder.create(this, APP_NAME + "S3")
                .path("../software/ElasticBeanstalk/target/elastic-beanstalk.jar")
                .build();

        // Create Elastic Beanstalk app
        CfnApplication cfnApplication = CfnApplication.Builder.create(this, APP_NAME)
                .applicationName(APP_NAME).build();

        // Create Elastic Beanstalk app version
        CfnApplicationVersion newsAppVersion = CfnApplicationVersion.Builder.create(this,
                        APP_NAME + "Version")
                .applicationName(APP_NAME)
                .sourceBundle(CfnApplicationVersion.SourceBundleProperty.builder()
                        .s3Bucket(asset.getS3BucketName())
                        .s3Key(asset.getS3ObjectKey())
                        .build())
                .build();

        // Connect Elastic Beanstalk resource with S3 resource
        newsAppVersion.addDependency(cfnApplication);

        // Create role for ElasticBeanstalk service
        Role role = Role.Builder.create(this, "aws-elasticbeanstalk-service-role")
                .assumedBy(new ServicePrincipal("ec2.amazonaws.com"))
                .build();

        // Add appropriate permission policy
        role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("AWSElasticBeanstalkWebTier"));
        role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("AmazonDynamoDBFullAccess"));
        role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("SecretsManagerReadWrite"));

        // Create instance profile for EC2 instances
        String EC2instanceProfile = "aws-elasticbeanstalk-ec2-role";
        CfnInstanceProfile.Builder.create(this, EC2instanceProfile)
                .instanceProfileName(EC2instanceProfile)
                .roles(List.of(role.getRoleName()))
                .build();

        // Create Environment properties
        OptionSettingProperty iamInstanceProfile = createOptionSettings(
                "aws:autoscaling:launchconfiguration",
                "IamInstanceProfile",
                EC2instanceProfile);

        OptionSettingProperty environmentProperties = createOptionSettings(
                "aws:elasticbeanstalk:application:environment",
                "EnvironmentProperties",
                null);

        // Create Elastic Beanstalk environment
        CfnEnvironment.Builder.create(this, "Environment")
                .applicationName(APP_NAME)
                .environmentName(APP_NAME + "Env")
                .solutionStackName("64bit Amazon Linux 2023 v4.2.7 running Corretto 17")
                .versionLabel(newsAppVersion.getRef())
                .optionSettings(List.of(iamInstanceProfile, environmentProperties))
                .cnamePrefix(APP_NAME.toLowerCase())
                .build();
    }

    public OptionSettingProperty createOptionSettings(String namespace, String optionName, String value) {
        return OptionSettingProperty.builder()
                .namespace(namespace)
                .optionName(optionName)
                .value(value)
                .build();
    }
}
