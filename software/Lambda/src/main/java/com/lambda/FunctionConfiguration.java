package com.lambda;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.lambda"})
public class FunctionConfiguration {
    public static void main(String[] args) {
    }

    @Bean
    public java.util.function.Function<String, String> uppercase() {
        return value -> "test";
    }
}