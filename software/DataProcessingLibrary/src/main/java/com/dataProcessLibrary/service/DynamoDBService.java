package com.dataProcessLibrary.service;

import com.dataProcessLibrary.dto.City;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.concurrent.CompletableFuture;

public class DynamoDBService {
    public final TableSchema<City> tableSchema = TableSchema.fromBean(City.class);
    public final DynamoDbEnhancedAsyncClient enhancedAsyncClient =
            DynamoDbEnhancedAsyncClient.builder()
                    .dynamoDbClient(DynamoDbAsyncClient.builder().build())
                    .build();
    public final DynamoDbAsyncTable<City> asyncTable = enhancedAsyncClient.table("News", tableSchema);

    public final DynamoDbEnhancedClient enhancedClient =
            DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(DynamoDbClient.builder().build())
                    .build();
    public final DynamoDbTable<City> table = enhancedClient.table("News", tableSchema);

    public CompletableFuture<City> getNews(City city) {
        Key key = Key.builder()
                .partitionValue(city.getName())
                .sortValue(city.getState())
                .build();
        return asyncTable.getItem(key);
    }

    public PageIterable<City> getAllNews() {
        return table.scan();
    }

    public PagePublisher<City> getAllNewsAsync() {
        return asyncTable.scan();
    }

    public void putNews(City news) {
        asyncTable.putItem(news);
    }
}


