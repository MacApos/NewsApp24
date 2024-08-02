package com.myorg;

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
import java.util.Map;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }


    public OptionSettingProperty createOptionSettings(String namespace, String optionName, String value) {
        return OptionSettingProperty.builder()
                .namespace(namespace)
                .optionName(optionName)
                .value(value)
                .build();
    }

    public InfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Create S3 asset from zip file
        Asset S3 = Asset.Builder.create(this, "NewsAppS3")
                .path("../software/ElasticBeanstalk/target/elastic-beanstalk.jar")
                .build();

        // Create Elastic Beanstalk app
        String appName = "NewsApp";
        CfnApplication cfnApplication = CfnApplication.Builder.create(this, appName)
                .applicationName(appName).build();

        // Create Elastic Beanstalk app version
        CfnApplicationVersion newsAppVersion = CfnApplicationVersion.Builder.create(this, "NewsAppVersion")
                .applicationName(appName)
                .sourceBundle(CfnApplicationVersion.SourceBundleProperty.builder()
                        .s3Bucket(S3.getS3BucketName())
                        .s3Key(S3.getS3ObjectKey())
                        .build())
                .build();


        // Connect Elastic Beanstalk resource with S3 resource
        newsAppVersion.addDependency(cfnApplication);

        // Create role for ElasticBeanstalk service
        Role role = Role.Builder.create(this, "aws-elasticbeanstalk-service-role")
                .assumedBy(new ServicePrincipal("ec2.amazonaws.com"))
                // ServicePrincipal.Builder.create("ec2.amazonaws.com").build())
                .build();

        // Add appropriate permission policy
        role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("AWSElasticBeanstalkWebTier"));

        // Create instance profile for EC2 instances
        String EC2instanceProfile = "aws-elasticbeanstalk-ec2-role";
        CfnInstanceProfile build = CfnInstanceProfile.Builder.create(this, EC2instanceProfile)
                .instanceProfileName(EC2instanceProfile)
                .roles(List.of(role.getRoleName()))
                .build();

        // Create Environment properties
        OptionSettingProperty iamInstanceProfile = createOptionSettings(
                "aws:autoscaling:launchconfiguration",
                "IamInstanceProfile",
                EC2instanceProfile);

        OptionSettingProperty environmentProperties = createOptionSettings("aws:elasticbeanstalk:application:environment",
                "EnvironmentProperties", null);

        // Create Elastic Beanstalk environment
        String envName = appName + "Env";
        CfnEnvironment.Builder.create(this, "Environment")
                .applicationName(appName)
                .environmentName(envName)
                .solutionStackName("64bit Amazon Linux 2023 v4.2.7 running Corretto 17")
                .versionLabel(newsAppVersion.getRef())
                .optionSettings(List.of(iamInstanceProfile, environmentProperties))
                .cnamePrefix("newsapp24")
                .build();

    }
}
