package com.library;

import com.library.dto.City;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
public class DbConfig {
    @Bean
    public TableSchema<City> tableSchemaLib() {
        return TableSchema.fromBean(City.class);
    }

    @Bean
    public DynamoDbEnhancedAsyncClient enhancedClientLib() {
        DynamoDbAsyncClient asyncClient = DynamoDbAsyncClient.builder().build();
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(asyncClient)
                .build();
    }

    @Bean
    public DynamoDbAsyncTable<City> tableLib(DynamoDbEnhancedAsyncClient enhancedClient,
                                             TableSchema<City> tableSchema) {
        return enhancedClient.table("News", tableSchema);
    }
}
