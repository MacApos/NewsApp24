package com.infrastructure;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.events.Rule;
import software.amazon.awscdk.services.events.Schedule;
import software.amazon.awscdk.services.events.targets.LambdaFunction;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

import static com.infrastructure.InfrastructureApp.*;

public class LambdaStack extends Stack {
    public LambdaStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        String functionName = APP_NAME + "Lambda";
        String cronRuleName = functionName + "CronRule";

        // Lambda function resources
        Function saveDataFunction = Function.Builder.create(this, functionName)
                .functionName(functionName)
                .runtime(Runtime.JAVA_17)
                .code(Code.fromAsset("../software/Lambda/target/lambda.jar"))
                .handler("com.lambda.Handler")
                .timeout(Duration.seconds(15))
                .memorySize(512)
                .build();

        // Add appropriate permission policy to function role
        IRole role = saveDataFunction.getRole();
        if (role != null) {
            role.addManagedPolicy(ManagedPolicy.fromAwsManagedPolicyName("AmazonDynamoDBFullAccess"));
        }

        // Rule for the schedule that runs every day at 12:00 PM
        Rule rule = Rule.Builder.create(this, cronRuleName)
                .ruleName(cronRuleName)
                .description("Run every day at 12:00 PM UTC")
                .schedule(Schedule.expression("cron(0 12 * * ? *)"))
                .build();

        rule.addTarget(new LambdaFunction(saveDataFunction));
    }
}
