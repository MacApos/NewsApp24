package com.elasticBeanstalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication(scanBasePackages = {"com.elasticBeanstalk", "com.dataProcessingLibrary"})
public class ElasticBeanstalkApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(ElasticBeanstalkApplication.class, args);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

}
