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
        return Mono.fromFuture(dynamoDbService.getNews(name))
                .switchIfEmpty(processDataService.validateCity(name, state)
                        .flatMap(fetchDataService::fetchCity)
                        .doOnNext(dynamoDbService::putNews)
                );
    }

}
