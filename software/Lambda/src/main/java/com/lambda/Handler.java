//package com.lambda;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
//import com.dataProcessingLibrary.dao.Article;
//import com.dataProcessingLibrary.dao.City;
//import com.dataProcessingLibrary.service.ArticleService;
//import com.dataProcessingLibrary.service.DynamoDBService;
//import com.dataProcessingLibrary.service.FetchDataService;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Collection;
//import java.util.List;
//
//public class Handler implements RequestStreamHandler {
//    private final FetchDataService fetchDataService;
//    private final ArticleService articleService;
//
//    public static final List<City> initialCities = List.of(
//            new City("New York", "New York"),
//            new City("Ashburn", "Virginia"),
//            new City("Hemingford", "Nebraska")
//    );
//
//    public Handler(FetchDataService fetchDataService, ArticleService articleService) {
//        this.fetchDataService = fetchDataService;
//        this.articleService = articleService;
//    }
//
//    @Override
//    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
//        List<Article> all = articleService.findAll();
////        dynamoDBService.getAllNews().stream()
////                .map(cityPage -> cityPage.items().isEmpty() ? initialCities : cityPage.items())
////                .flatMap(Collection::stream)
////                .forEach(city -> {
////                    City fetch = fetchDataService.fetchNews(city).block();
////                    if (fetch != null) {
////                        fetch.addArticles(city.getArticles());
////                        fetch.sortArticles();
////                        dynamoDBService.table.putItem(fetch);
////                    }
////                });
//    }
//}


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