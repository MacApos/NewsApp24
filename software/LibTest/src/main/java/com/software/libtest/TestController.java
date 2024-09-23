package com.software.libtest;

import com.dataProcessLibrary.dao.City;
import com.dataProcessLibrary.service.FetchDataService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final FetchDataService fetchDataService;

    public TestController(FetchDataService fetchDataService) {
        this.fetchDataService = fetchDataService;
    }

    @RequestMapping("test")
    public String test(){
        City city = new City("New York", "New York");
        fetchDataService.fetchNews(city);
        return "test";
    }
}
