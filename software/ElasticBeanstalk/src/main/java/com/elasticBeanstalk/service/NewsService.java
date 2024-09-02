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

    public Mono<City> putNewsIntoTable(City city) {
        Mono<City> cityMono = processDataService.validateCity(city)
                .flatMap(validCity -> Mono.fromFuture(dynamoDbService.getNews(validCity)));

        return processDataService.validateCity(city)
                .flatMap(validCity -> Mono.fromFuture(dynamoDbService.getNews(validCity))
                        .switchIfEmpty(fetchDataService
                                .fetchNews(validCity)
//                                .mockFetchNews(validCity, "recent")
                                .doOnNext(dynamoDbService::putNews)
                        )
                );
    }

}
