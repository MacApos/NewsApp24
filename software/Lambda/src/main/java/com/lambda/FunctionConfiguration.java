package com.lambda;

import com.dataProcessingLibrary.service.ArticleService;
import com.dataProcessingLibrary.service.FetchDataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.lambda", "com.dataProcessingLibrary"})
public class FunctionConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(FunctionConfiguration.class, args);
    }


    @Bean
    public Function<String, String> uppercase(ArticleService articleService,
                                              FetchDataService fetchDataService) {

        return value -> value.toUpperCase();
    }
}