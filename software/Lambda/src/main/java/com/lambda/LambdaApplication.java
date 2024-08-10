package com.lambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.lambda", "com.library"})
public class LambdaApplication {
    public static void main(String[] args) {
        SpringApplication.run(LambdaApplication.class, args);
    }
}
