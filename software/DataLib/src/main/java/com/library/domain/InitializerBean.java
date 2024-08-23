package com.library.domain;

import com.library.service.DynamoDBService;
import com.library.service.FetchDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitializerBean {

    private final DynamoDBService dynamoDBService;
    private final FetchDataService fetchDataService;

    public InitializerBean(DynamoDBService dynamoDBService, FetchDataService fetchDataService) {
        this.dynamoDBService = dynamoDBService;
        this.fetchDataService = fetchDataService;
    }

    public LambdaFunction initClass() {
        return new LambdaFunction(this.dynamoDBService, this.fetchDataService);
    }
}