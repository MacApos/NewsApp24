package com.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.dataProcessLibrary.dto.City;
import com.dataProcessLibrary.service.DynamoDBService;
import com.dataProcessLibrary.service.FetchDataService;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

public class Handler implements RequestStreamHandler {
    private static final DynamoDBService dynamoDBService = new DynamoDBService();
    private static final FetchDataService fetchDataService = new FetchDataService();
    public static final List<City> initialCities = List.of(
            new City("New York", "New York")
//            new City("Ashburn", "Virginia"),
//            new City("Hemingford", "Nebraska")
    );

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
        dynamoDBService.getAllNews().stream()
                .map(cityPage -> cityPage.items().isEmpty() ? initialCities : cityPage.items())
                .flatMap(Collection::stream)
                .forEach(city -> {
                    City fetch = fetchDataService.fetchNews(city).block();
                    if (fetch != null) {
                        fetch.addArticles(city.getArticles());
                        fetch.sortArticles();
                        dynamoDBService.table.putItem(fetch);
                    }
                });
    }
}
