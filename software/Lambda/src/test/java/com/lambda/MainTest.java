package com.lambda;

import com.lambda.dto.City;
import com.lambda.service.DynamoDBService;
import com.lambda.service.FetchDataService;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;

import static com.lambda.Handler.initialCities;

class MainTest {
    private final DynamoDBService dynamoDBService = new DynamoDBService();
    private static final FetchDataService fetchDataService = new FetchDataService();

    @Test
    void syncTest() {
        dynamoDBService.getAllNews().stream()
                .map(cityPage -> cityPage.items().isEmpty() ? initialCities : cityPage.items())
                .flatMap(Collection::stream)
                .forEach(city -> {
                    City fetch = fetchDataService.mockFetchNews(city).block();
                    if (fetch != null) {
                        fetch.addArticles(city.getArticles());
                        fetch.sortArticles();
                        dynamoDBService.table.putItem(fetch);
                    }
                });
    }

    @Test
    void asyncTest() {
        Mono.from(dynamoDBService.getAllNewsAsync())
                .map(cityPage -> cityPage.items().isEmpty() ? initialCities : cityPage.items())
                .flatMapMany(Flux::fromIterable)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(city -> {
                    City fetch = fetchDataService.mockFetchNews(city).block();
                    if (fetch != null) {
                        fetch.addArticles(city.getArticles());
                        fetch.sortArticles();
                        dynamoDBService.table.putItem(fetch);
                    }
                })
                .then()
                .block();
    }
}