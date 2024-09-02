package com.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.dto.City;
import com.lambda.service.DynamoDBService;
import com.lambda.service.FetchDataService;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class Handler implements RequestStreamHandler {
    private static final DynamoDBService dynamoDBService = new DynamoDBService();
    private static final FetchDataService fetchDataService = new FetchDataService();
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final List<City> initialCities = List.of(new City("New York", "New York"),
            new City("Ashburn", "Virginia"),
            new City("Hemingford", "Nebraska"));

    public static Mono<City> fetchCity(City city, Map<String, String> newsMap) {
        String news = newsMap.get(city.getName());
        City newCity;
        try {
            newCity = objectMapper.readValue(news, City.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Mono.just(newCity);
    }

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
