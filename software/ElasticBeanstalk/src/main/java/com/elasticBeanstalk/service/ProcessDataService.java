package com.elasticBeanstalk.service;

import com.elasticBeanstalk.dao.News;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static com.elasticBeanstalk.service.FetchDataService.TRENDING;

@Service
public class ProcessDataService {
    private final FetchDataService fetchDataService;
    private final NewsService newsService;
    private final ArticleService articleService;
    private final Validator validator;
    private final String COUNTRY_CODE = "US";
    private final ResponseStatusException CITY_NOT_FOUND =
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "City not found");

    public ProcessDataService(ArticleService articleService, FetchDataService fetchDataService,
                              NewsService newsService, Validator validator) {
        this.articleService = articleService;
        this.fetchDataService = fetchDataService;
        this.newsService = newsService;
        this.validator = validator;
    }

    private Mono<News> validateCity(News news) {
        return Mono.just(news)
                // Validation
                .flatMap(initialCity -> {
                    Errors errors = new BeanPropertyBindingResult(initialCity,
                            News.class.getName());
                    validator.validate(initialCity, errors);
                    if (errors.getAllErrors().isEmpty()) {
                        Mono<News> newsMono = fetchDataService.fetchCity(initialCity.prepareQuery(COUNTRY_CODE));
                        return newsMono;
                    }
                    return Mono.error(CITY_NOT_FOUND);
                })
                // Fetching
                .flatMap(validCity -> {
                    // Data passed validation but city wasn't found
                    if (validCity.getCityName() == null) {
                        return Mono.error(CITY_NOT_FOUND);
                    }
                    return Mono.just(validCity);
                });
    }

    private Mono<News> getOrFetchNews(News news) {
        News newsByCityName = newsService.findNews(news);
        if (newsByCityName == null) {
            Mono<News> newsMono = fetchDataService.fetchNews(news)
                    .filter(fetchedNews -> !fetchedNews.getArticles().isEmpty())
                    .doOnNext(newsService::saveNews);
            return newsMono;
        }
        Mono<News> just = Mono.just(newsByCityName);
        return just;
    }

    public Mono<News> getNewsByCity(News news) {
        Mono<News> newsMono = validateCity(news)
                .flatMap((n) -> {
                    Mono<News> orFetchNews = getOrFetchNews(n);
                    return orFetchNews;
                });
        return newsMono;
    }

    public Mono<News> getTrending() {
        News city = new News(TRENDING, "-");
        return getOrFetchNews(city);
    }

}
