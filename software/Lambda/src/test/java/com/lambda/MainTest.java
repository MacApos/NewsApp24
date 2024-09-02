package com.lambda;

import com.lambda.dto.Article;
import com.lambda.dto.City;
import com.lambda.service.DynamoDBService;
import com.lambda.service.FetchDataService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static com.lambda.Handler.initialCities;
import static com.lambda.service.FetchDataService.*;

class MainTest {
    private final DynamoDBService dynamoDBService = new DynamoDBService();
    private static final FetchDataService fetchDataService = new FetchDataService();

    public Mono<String> prepareStrResponse(String host, String path, Map<String, String> params,
                                           Map<String, String> headers) {
        HttpRequest request = fetchDataService.prepareRequest(host, path, params, headers);
        return Mono.fromFuture(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
                .map(HttpResponse::body);
    }

    public Mono<String> fetchStrCity(City city) {
        LinkedHashMap<String, String> qParam = new LinkedHashMap<>(Map.of("q", city.prepareQuery()));
        qParam.put("offset", "10");
        qParam.putAll(NEWS_API_URI_PARAMS);
        return prepareStrResponse(NEWS_HOST, NEWS_PATH, qParam, NEWS_API_URI_HEADERS);
    }

    @BeforeAll
    static void populateInitialCities() {
        FetchDataService staticFetchDataService = new FetchDataService();
        initialCities.forEach(city -> {
            City block = staticFetchDataService.mockFetchNews(city, "old").block();
            if (block != null) {
                city.updateCity(block);
            }
        });
    }

//    @Test
//    void mockJson() {
//        String collect = initialCities.stream()
//                .map(city -> String.format("\"%s\",\"%s\"",
//                        city.getName(), fetchStrCity(city).block()))
//                .collect(Collectors.joining("\n"));
//        System.out.println(collect);
//    }


    @Test
    void syncTest() {
        dynamoDBService.getAllNews().stream()
                .map(cityPage -> cityPage.items().isEmpty() ? initialCities : cityPage.items())
                .flatMap(Collection::stream)
                .forEach(city -> {
                    City fetch = fetchDataService.fetchNews(city).block();

                    if (fetch != null) {
                        fetch.addArticles(city.getArticles());
                        fetch.sortArticles();
                        city = fetch;
                    }
                    dynamoDBService.table.putItem(city);
                });
    }

    @Test
    void asyncTest() {
        Mono.from(dynamoDBService.getAllNewsAsync())
                .map(cityPage -> cityPage.items().isEmpty() ? initialCities : cityPage.items())
                .flatMapMany(Flux::fromIterable)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(city -> {
                    City block = fetchDataService.mockFetchNews(city, "recent").block();
                    if (block != null) {
                        block.updateCity(city);
                    }
                    dynamoDBService.table.putItem(block);
                })
                .then()
                .block();
    }
}