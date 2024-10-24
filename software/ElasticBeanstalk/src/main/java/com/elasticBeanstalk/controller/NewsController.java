package com.elasticBeanstalk.controller;

import com.elasticBeanstalk.dao.News;
import com.elasticBeanstalk.service.ProcessDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class NewsController {
    private final ProcessDataService processDataService;

    public NewsController(ProcessDataService processDataService) {
        this.processDataService = processDataService;
    }

    @RequestMapping("/")
    public HttpStatus home() {
        return HttpStatus.OK;
    }

    @RequestMapping(value = {"/{cityName}", "/{cityName}/{state}"})
    public Mono<News> fetchNewsByCityName(@PathVariable String cityName,
                                          @PathVariable(required = false) String state) {
        return processDataService.getNewsByCityName(new News(cityName, state));
    }

    @RequestMapping(value = {"/trending"})
    public Mono<News> getTrending() {
        return processDataService.getTrending();
    }

    @PostMapping("/update")
    public String update() {
        processDataService.fetchAndUpdateNews();
        return "updated";
    }

    @GetMapping("/test-update")
    public String testUpdate() {
        processDataService.fetchAndUpdateNews();
        return "updated";
    }
}