package com.newsapp24.elasticbeanstalk.domain;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControllerController {
    @RequestMapping("/test")
    public String testController() {
        return "test";
    }
}