package com.elasticBeanstalk.service;

import com.dataProcessingLibrary.dao.City;
import com.dataProcessingLibrary.service.FetchDataService;
import com.dataProcessingLibrary.service.DynamoDBService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.dataProcessingLibrary.service.FetchDataService.TRENDING;

@Service
public class NewsService {
    private final FetchDataService fetchDataService;
    private final DynamoDBService dynamoDbService = new DynamoDBService();
    private final ProcessDataService processDataService;

    public NewsService(FetchDataService fetchDataService, ProcessDataService processDataService) {
        this.fetchDataService = fetchDataService;
        this.processDataService = processDataService;
    }

    private Mono<City> getOrFetchNews(City city) {
        return Mono.fromFuture(dynamoDbService.getNews(city))
                .switchIfEmpty(fetchDataService.fetchNews(city)
                        .filter(fetchedCity -> !fetchedCity.getArticles().isEmpty())
                        .doOnNext(dynamoDbService::putNews)
                );
    }

    public Mono<City> getTrending() {
        City city = new City(TRENDING, "-");
        return getOrFetchNews(city);
    }

    public Mono<City> getNewsByCity(City city) {
        return processDataService.validateCity(city);
//        return processDataService.validateCity(city)
//                .flatMap(this::getOrFetchNews);
    }

}
