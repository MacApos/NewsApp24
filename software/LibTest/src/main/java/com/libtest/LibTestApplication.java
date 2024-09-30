package com.libtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.libtest", "com.dataProcessingLibrary"})
public class LibTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibTestApplication.class, args);
    }

}
