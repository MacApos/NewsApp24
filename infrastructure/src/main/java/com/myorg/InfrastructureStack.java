package com.myorg;

import software.amazon.awscdk.services.elasticbeanstalk.CfnApplication;
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplicationVersion;
import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironment;
import software.amazon.awscdk.services.iam.CfnInstanceProfile;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.s3.assets.Asset;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.List;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Create S3 asset from zip file
        Asset S3 = Asset.Builder.create(this, "NewsAppS3")
                .path("../software/ElasticBeanstalk/assets/elasticBeanstalk.jar")
                .build();

        // Create Elastic Beanstalk app
        String appName = "NewsApp";
        CfnApplication cfnApplication = CfnApplication.Builder.create(this, appName).build();

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
        String ec2RoleName = "aws-elasticbeanstalk-ec2-role";
        CfnInstanceProfile.Builder.create(this, ec2RoleName)
                .instanceProfileName(ec2RoleName)
                .roles(List.of(role.getRoleName()))
                .build();

        // Create Elastic Beanstalk environment
        String envName = appName + "Env";
        CfnEnvironment.Builder.create(this, envName)
                .applicationName(appName)
                .environmentName(envName)
                .solutionStackName("64bit Amazon Linux 2023 v5.3.0 running Tomcat 10 Corretto 17")
                .versionLabel(newsAppVersion.getRef())
                .build();


//initialize
    }
}
