package com.lambda;

import com.library.service.FetchDataService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = {"com.lambda", "com.library"})
public class LambdaApplication {
    public static void main(String[] args) {
//        SpringApplication.run(LambdaApplication.class, args);
    }

    private final FetchDataService fetchDataService;

    public LambdaApplication(FetchDataService fetchDataService) {
        this.fetchDataService = fetchDataService;
    }

    @Bean
    public Function<Map<String, String>, String> uppercase() {
        return map -> map.entrySet()
                .stream()
                .map(entry -> entry.getKey() + entry.getValue())
                .collect(Collectors.joining(""));
    }
}
