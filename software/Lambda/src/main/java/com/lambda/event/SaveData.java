package com.lambda.event;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaveData {
    @RequestMapping("/")
    public String test() {
        return "test";
    }
}