package com.infrastructure;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class LambdaStack extends Stack {
    public LambdaStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        String functionName = "SaveDataFunction";

        // Lambda function resources
        Function saveDataFunction = Function.Builder.create(this, functionName)
                .runtime(Runtime.JAVA_17)
//                .code(Code.fromAsset("../software/Lambda/target/lambda.jar"))
                .code(Code.fromAsset("../../spring-cloud-function/spring-cloud-function-main/spring-cloud-function-samples/function-sample-aws/target/function-sample-aws-0.0.1-SNAPSHOT.jar"))
                .handler("org.springframework.cloud.function.adapter.aws.FunctionInvoker")
                .build();

        // Lambda function URL resource
        FunctionUrl functionUrl = saveDataFunction.addFunctionUrl(FunctionUrlOptions.builder()
                .authType(FunctionUrlAuthType.NONE)
                .build());

        // CloudFormation output for URL
        CfnOutput.Builder.create(this, saveDataFunction+"Url")
                .value(functionUrl.getUrl())
                .build();
    }
}
