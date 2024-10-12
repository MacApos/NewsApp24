package com.libtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.libtest"})
public class LibTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibTestApplication.class, args);
    }
}
