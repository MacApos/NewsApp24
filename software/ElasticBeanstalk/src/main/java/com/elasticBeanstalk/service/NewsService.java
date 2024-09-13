package com.elasticBeanstalk.service;

import com.lambda.dto.City;
import com.lambda.service.FetchDataService;
import com.lambda.service.DynamoDBService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.lambda.service.FetchDataService.TRENDING;

@Service
public class NewsService {
    private final FetchDataService fetchDataService = new FetchDataService();
    private final DynamoDBService dynamoDbService = new DynamoDBService();
    private final ProcessDataService processDataService;

    public NewsService(ProcessDataService processDataService) {
        this.processDataService = processDataService;
    }

    private Mono<City> getOrFetchNews(City city) {
        return Mono.fromFuture(dynamoDbService.getNews(city))
                .switchIfEmpty(fetchDataService
                .fetchNews(city)
                .doOnNext(dynamoDbService::putNews)
        );
    }

    public Mono<City> getTrending() {
        City city = new City(TRENDING, "-");
        return getOrFetchNews(city);
    }

    public Mono<City> getNewsByCity(City city) {
        return processDataService.validateCity(city).flatMap(this::getOrFetchNews);
    }

}
