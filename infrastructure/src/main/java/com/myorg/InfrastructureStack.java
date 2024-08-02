package com.myorg;

import software.amazon.awscdk.services.s3.assets.Asset;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import static java.lang.String.join;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.elasticbeanstalk.*;

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
        String appName = "NewsAppEB";
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

        

    }
}
