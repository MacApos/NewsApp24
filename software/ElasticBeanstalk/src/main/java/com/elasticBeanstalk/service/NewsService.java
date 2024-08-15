package com.elasticBeanstalk.service;

import com.library.dto.City;
import com.library.service.FetchDataService;
import com.library.service.DynamoDBService;
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

    public Mono<Object> putNewsIntoTable(String name, String state) {
//        Mono<City> cityMono = Mono.fromFuture(dynamoDbService.getNews(name))
//                .switchIfEmpty(processDataService.validateCity(name, state));
        return processDataService.validateCity(name, state);
//                        .flatMap(city -> fetchDataService.fetchNews(city.prepareQuery()))
//                        .doOnNext(dynamoDbService::putNews)
    }

}
