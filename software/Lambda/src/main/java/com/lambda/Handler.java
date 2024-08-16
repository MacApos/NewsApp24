package com.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.library.service.DynamoDBService;
import com.library.service.FetchDataService;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Random;

public class Handler implements RequestHandler<Map<String, String>, Integer> {


    @Override
    public Integer handleRequest(Map<String, String> stringStringMap, Context context) {
        Random random = new Random();
        return random.nextInt();
    }
}
