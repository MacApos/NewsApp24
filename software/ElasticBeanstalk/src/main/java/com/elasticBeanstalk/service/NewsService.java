package com.elasticBeanstalk.service;

import com.elasticBeanstalk.dao.Article;
import com.elasticBeanstalk.dao.News;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class NewsService {
    private final FetchDataService fetchDataService;
    private final ProcessDataService processDataService;
    private final CityService cityService;
    private final ArticleService articleService;

    public NewsService(FetchDataService fetchDataService, ProcessDataService processDataService, CityService cityService, ArticleService articleService) {
        this.fetchDataService = fetchDataService;
        this.processDataService = processDataService;
        this.cityService = cityService;
        this.articleService = articleService;
    }

    private Mono<News> getOrFetchNews(News news) {
        News newsByCityName = cityService.findCity(news);
        if (newsByCityName == null) {
            Mono<News> newsMono = fetchDataService.fetchNews(news)
                    .filter(fetchedNews -> !fetchedNews.getArticles().isEmpty())
                    .doOnNext(fetchedNews->{
                        cityService.saveCity(news);
                        fetchedNews.getArticles().forEach(articleService::saveArticle);
                    })
                    ;
            return newsMono;
        }
        Mono<News> just = Mono.just(newsByCityName);
        return just;
    }

//    public Mono<City> getTrending() {
//        City city = new City(TRENDING, "-");
//        return getOrFetchNews(city);
//    }

    public Mono<News> getNewsByCity(News news) {
        Mono<News> newsMono = processDataService.validateCity(news)
                .flatMap(this::getOrFetchNews);
        return newsMono;
    }

}
