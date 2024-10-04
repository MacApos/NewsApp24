package com.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication( scanBasePackages = {"com.lambda", "com.dataProcessingLibrary"})
public class FunctionConfiguration {
    public static void main(String[] args) {
    }

    @Bean
    public java.util.function.Function<String, String> uppercase() {
        return value -> {
            if (value.equals("exception")) {
                throw new RuntimeException("Intentional exception");
            }
            else {
                return value.toUpperCase();
            }
        };
    }
}