package com.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = {"com.lambda", "com.library"})
public class LambdaApplication {
    public static void main(String[] args) {
        SpringApplication.run(LambdaApplication.class, args);
    }

    @Bean
    public Function<Map<String, String>, String> uppercase() {
        return map -> map.entrySet()
                .stream()
                .map(entry -> entry.getKey() + entry.getValue())
                .collect(Collectors.joining(""));
    }
}
