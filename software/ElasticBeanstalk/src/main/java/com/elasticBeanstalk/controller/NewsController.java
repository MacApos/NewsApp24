package com.elasticBeanstalk.controller;

import com.elasticBeanstalk.service.NewsService;
import com.lambda.dto.City;
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

    @RequestMapping(value = {"/{name}", "/{name}/{state}"})
    public Mono<City> newsController(@PathVariable String name,
                                     @PathVariable(required = false) String state) {
        return newsService.putNewsIntoTable(new City(name, state));
    }
}