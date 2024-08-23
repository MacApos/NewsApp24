package com.library.domain;

import com.library.service.DynamoDBService;
import com.library.service.FetchDataService;

public class LambdaFunction {
    private final DynamoDBService dynamoDBService;
    private final FetchDataService fetchDataService;

    public LambdaFunction(DynamoDBService dynamoDBService, FetchDataService fetchDataService) {
        this.dynamoDBService = dynamoDBService;
        this.fetchDataService = fetchDataService;
    }
}
