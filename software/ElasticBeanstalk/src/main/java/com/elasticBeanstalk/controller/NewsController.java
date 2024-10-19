package com.elasticBeanstalk.controller;

import com.elasticBeanstalk.dao.News;
import com.elasticBeanstalk.service.ProcessDataService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class NewsController {
    private final ProcessDataService processDataService;

    public NewsController(ProcessDataService processDataService) {
        this.processDataService = processDataService;
    }

    @RequestMapping(value = {"/{cityName}", "/{cityName}/{state}"})
    public Mono<News> fetchNewsByCityName(@PathVariable String cityName,
                                          @PathVariable(required = false) String state) {
        return processDataService.getNewsByCity(new News(cityName, state));
    }

//    @RequestMapping(value = {"/trending"})
//    public Mono<News> getTrending() {
//        return newsService.getTrending();
//    }
}