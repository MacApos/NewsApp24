package com.example.service;

import com.library.service.FetchDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Test {
    @Autowired
    private final FetchDataService fetchDataService;

    public Test(FetchDataService fetchDataService) {
        this.fetchDataService = fetchDataService;
    }
}
