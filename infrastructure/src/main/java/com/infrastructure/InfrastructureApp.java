package com.infrastructure;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awssdk.regions.Region;

public class InfrastructureApp {
    public static final String REGION = Region.EU_CENTRAL_1.toString();
    public static final String APP_NAME = "NewsApp24";
    public static final String ROOT_DOMAIN = "newsapp24.com";
    public static final String HOSTED_ZONE_ID = "Z0954086RSQN4LCK08H6";

    public static void main(final String[] args) {
        App app = new App();
        Environment environment = Environment.builder()
                .region(REGION)
                .build();

        StackProps stackProps = StackProps.builder().build();
        new ElasticBeanstalkStack(app, "ElasticBeanstalkStack", stackProps);
        new LambdaStack(app, "LambdaStack", stackProps);
        new StaticSiteStack(app, "StaticSiteStack", StackProps.builder().env(environment).build());

        app.synth();
    }
}

