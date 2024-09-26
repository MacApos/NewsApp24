package com.dataProcessingLibrary;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@AutoConfigurationPackage
public class DataProcessingLibraryConfiguration {
    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }
}
