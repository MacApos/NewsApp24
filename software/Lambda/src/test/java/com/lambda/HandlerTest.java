//package com.lambda;
//
//import com.dataProcessLibrary.dao.City;
//import com.dataProcessLibrary.service.DynamoDBService;
//import com.dataProcessLibrary.service.FetchDataService;
//import org.junit.jupiter.api.Test;
//
//import java.util.Collection;
//
//import static com.lambda.Handler.initialCities;
//
//class HandlerTest {
//    private final DynamoDBService dynamoDBService = new DynamoDBService();
//    private static final FetchDataService fetchDataService = new FetchDataService();
//
//    @Test
//    void putItemsSyncTest() {
//        dynamoDBService.getAllNews().stream()
//                .map(cityPage -> cityPage.items().isEmpty() ? initialCities : cityPage.items())
//                .flatMap(Collection::stream)
//                .forEach(city -> {
//                    City fetch = fetchDataService.fetchNews(city).block();
//                    if (fetch != null) {
//                        fetch.addArticles(city.getArticles());
//                        fetch.sortArticles();
//                        dynamoDBService.table.putItem(fetch);
//                    }
//                });
//    }
//}