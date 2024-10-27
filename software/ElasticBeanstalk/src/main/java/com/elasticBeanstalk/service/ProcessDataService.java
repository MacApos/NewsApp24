package com.elasticBeanstalk.service;

import com.elasticBeanstalk.dao.Article;
import com.elasticBeanstalk.dao.News;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.elasticBeanstalk.service.FetchDataService.countryCode;
import static com.elasticBeanstalk.service.FetchDataService.trending;

@Service
public class ProcessDataService {
    private final ResponseStatusException cityNotFound =
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "City not found");

    private final FetchDataService fetchDataService;
    private final NewsService newsService;
    private final Validator validator;
    private final ArticleService articleService;

    public ProcessDataService(FetchDataService fetchDataService, NewsService newsService, Validator validator, ArticleService articleService) {
        this.fetchDataService = fetchDataService;
        this.newsService = newsService;
        this.validator = validator;
        this.articleService = articleService;
    }

    // Mono.defer() ensures the whole block is lazily executed when the subscription happens
    private Mono<News> validateCity(News news) {
        return Mono.defer(() -> {
            Errors errors = new BeanPropertyBindingResult(news,
                    News.class.getName());
            validator.validate(news, errors);
            if (!errors.getAllErrors().isEmpty()) {
                return Mono.error(cityNotFound);
            }
            return fetchDataService.fetchCity(
                            news.prepareQuery() + "," + countryCode)
                    .flatMap(validCity -> {
                        // Data passed validation but city wasn't found
                        if (validCity.getCityName() == null) {
                            return Mono.error(cityNotFound);
                        }
                        return Mono.just(validCity);
                    });
        });
    }

    /*
    Mono.defer - ensures that fetchDataService.fetchNews(news) is invoked only when findMonoNews(news) is empty.
    Mono.defer delays the creation of the Mono until it's actually needed, thereby preventing eager execution.
     */
    private Mono<News> getOrFetchNews(News news) {
        return newsService.findNews(news)
                .switchIfEmpty(Mono.defer(() ->
                        fetchDataService.fetchNews(news)
                                .doOnNext(fetchedNews -> {
                                    fetchedNews.sortArticles();
                                    newsService.saveNews(fetchedNews);
                                })
                ));
    }

    public Mono<News> getNewsByCityName(News news) {
        return validateCity(news)
                .flatMap(this::getOrFetchNews);
    }

    public Mono<News> getTrending() {
        News city = new News(trending, "-");
        return getOrFetchNews(city);
    }

    public void fetchAndUpdateNews() {
        List<News> all = newsService.findAll();
        Flux.fromIterable(all)
                .flatMap(news -> fetchDataService.fetchNews(news)
                        .doOnNext(fetchedNews -> {
                            List<Article> newArticles = fetchedNews.getArticles();
                            List<Article> existingArticles = articleService.findAllByNews(news)
                                    .stream()
                                    .peek(article -> article.setId(null))
                                    .toList();

                            newArticles.addAll(existingArticles);
                            fetchedNews.setArticles(newArticles);
                            fetchedNews.sortArticles();

                            newsService.deleteNews(news);
                            newsService.saveNews(fetchedNews);
                        })
                )
                .subscribe();
    }
}
