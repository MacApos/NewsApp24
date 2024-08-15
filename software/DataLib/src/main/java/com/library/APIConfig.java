//package com.library;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class APIConfig {
//    @Value("${newsApiKey}")
//    public String newsApiKey;
//
//    @Bean
//    public WebClient webClient() {
//        return WebClient.create();
//    }
//
//    @Bean
//    public HashMap<String, String> newsApiUriHeaders() {
//        return new HashMap<>(Map.of("Ocp-Apim-Subscription-Key", newsApiKey));
//    }
//
//    @Bean
//    public HashMap<String, String> newsApiUriParams(){
//        return new HashMap<>(Map.of("count", "20",
//                "mkt", "en-US",
//                "setLang", "en",
//                "sortBy", "date",
//                "originalImg", "true"));
//
//    }
//}
