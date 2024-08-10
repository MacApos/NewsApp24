package com.elasticBeanstalk.service;

import com.library.dto.City;
import com.library.service.FetchDataService;
import com.library.service.DynamoDbService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class NewsService {
    private final FetchDataService fetchDataService;
    private final DynamoDbService dynamoDbService;
    private final ProcessDataService processDataService;

    public NewsService(DynamoDbService dynamoDbService, ProcessDataService processDataService, FetchDataService fetchDataService) {
        this.dynamoDbService = dynamoDbService;
        this.processDataService = processDataService;
        this.fetchDataService = fetchDataService;
    }

    public Mono<City> putNewsIntoTable(String name, String state) {
        return Mono.fromFuture(dynamoDbService.getNews(name))
                .switchIfEmpty(processDataService.validateCity(name, state)
                        .flatMap(city -> fetchDataService.fetchNews(city.prepareQuery()))
                        .doOnNext(dynamoDbService::putNews));
    }

}
