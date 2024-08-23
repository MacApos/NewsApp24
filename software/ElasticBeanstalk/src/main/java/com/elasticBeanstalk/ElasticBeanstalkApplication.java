package com.elasticBeanstalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.elasticBeanstalk", "com.lambda"})
public class ElasticBeanstalkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticBeanstalkApplication.class, args);
    }

}
