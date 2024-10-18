package com.elasticBeanstalk.controller;

import com.elasticBeanstalk.dao.News;
import com.elasticBeanstalk.service.NewsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @RequestMapping(value = {"/{cityName}", "/{cityName}/{state}"})
    public Mono<News> fetchNewsByCityName(@PathVariable String cityName,
                                          @PathVariable(required = false) String state) {
        return newsService.getNewsByCity(new News(cityName, state));
    }

//    @RequestMapping(value = {"/trending"})
//    public Mono<News> getTrending() {
//        return newsService.getTrending();
//    }
}