package com.infrastructure;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class InfrastructureApp {
    public static void main(final String[] args) {
        App app = new App();

        Environment environment = Environment.builder()
                .account("123456789012")
                .region("us-east-1")
                .build();


//        new ElasticBeanstalkStack(app, "ElasticBeanstalkStack", StackProps.builder()
////                .env(environment)
//                .build());

        new LambdaStack(app, "LambdaStack", StackProps.builder()
                .build());

        app.synth();
    }
}

