package com.elasticBeanstalk.service;

import com.lambda.dto.City;
import com.lambda.service.FetchDataService;
import com.lambda.service.DynamoDBService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NewsService {
    private final FetchDataService fetchDataService = new FetchDataService();
    private final DynamoDBService dynamoDbService = new DynamoDBService();
    private final ProcessDataService processDataService;

    public NewsService(ProcessDataService processDataService) {
        this.processDataService = processDataService;
    }

    public Mono<City> putNewsIntoTable(String name, String state) {
        Mono<City> cityMono = processDataService.validateCity(name, state)
                .flatMap(city -> Mono.fromFuture(dynamoDbService.getNews(city))
                        .switchIfEmpty(fetchDataService.mockFetchNews(city, "recent")
                                .doOnNext(dynamoDbService::putNews)));
        return cityMono;
    }

}
