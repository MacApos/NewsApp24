package com.infrastructure;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class InfrastructureApp {
    public static void main(final String[] args) {
        App app = new App();

        new ElasticBeanstalkStack(app, "InfrastructureStack", StackProps.builder()
                /*
                .env(Environment.builder()
                        .account("123456789012")
                        .region("us-east-1")
                        .build())
                 */
                .build());
        app.synth();
    }
}

