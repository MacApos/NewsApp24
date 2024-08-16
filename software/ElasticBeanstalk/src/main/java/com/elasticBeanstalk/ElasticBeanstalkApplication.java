package com.elasticBeanstalk;

import com.library.service.FetchDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.util.Map;


@SpringBootApplication(scanBasePackages = {"com.elasticBeanstalk", "com.library"})
public class ElasticBeanstalkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticBeanstalkApplication.class, args);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

}
