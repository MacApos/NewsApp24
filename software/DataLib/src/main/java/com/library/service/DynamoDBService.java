package com.library.service;

import com.library.dto.City;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.util.concurrent.CompletableFuture;

public class DynamoDBService {
    private final TableSchema<City> tableSchema = TableSchema.fromBean(City.class);
    private final DynamoDbEnhancedAsyncClient enhancedClient =
            DynamoDbEnhancedAsyncClient.builder()
                    .dynamoDbClient(DynamoDbAsyncClient.builder().build())
                    .build();
    private final DynamoDbAsyncTable<City> table = enhancedClient.table("News", tableSchema);

    public CompletableFuture<City> getNews(String cityName) {
        Key key = Key.builder().partitionValue(cityName).build();
        return table.getItem(key);
    }

    public PagePublisher<City> getAllNews() {
        return table.scan();
    }

    public void putNews(City news) {
        table.putItem(news);
    }

    public void deleteNews(String cityName) {

    }

    public void updateNews() {

    }

}


