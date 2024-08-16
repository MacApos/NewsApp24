package com.lambda;

import com.library.dto.City;
import com.library.service.DynamoDBService;
import com.library.service.FetchDataService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {


    private static final FetchDataService fetchDataService = new FetchDataService();
    private static final DynamoDBService dynamoDBService = new DynamoDBService();
    private static final TableSchema<City> tableSchema = TableSchema.fromBean(City.class);
    private static final DynamoDbEnhancedClient enhancedClient =
            DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(DynamoDbClient.builder().region(Region.EU_CENTRAL_1).build())
                    .build();

    private static final DynamoDbTable<City> table = enhancedClient.table("News", tableSchema);
    private static final List<City> initialCities = List.of(new City("New York", "New York"),
            new City("Ashburn", "Virginia"),
            new City("Hemingford", "Nebraska"));

    public static void main(String[] args) {
        WriteBatch.Builder<City> cities = WriteBatch.builder(City.class)
                .mappedTableResource(table);

        for (City city : initialCities) {
            cities.addPutItem(city);
        }
        BatchWriteResult batchWriteResult = enhancedClient.batchWriteItem(builder -> builder.writeBatches(cities.build()));
        List<City> unprocessedCities = batchWriteResult.unprocessedPutItemsForTable(table);
        if (!unprocessedCities.isEmpty()) {
            unprocessedCities.forEach(key -> System.out.println(key.toString()));
        }


    }
}
