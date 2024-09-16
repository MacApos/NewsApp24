package com.dataProcessLibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dataProcessLibrary.Secrets;
import com.dataProcessLibrary.dto.City;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FetchDataService {
    public static final HttpClient httpClient = HttpClient.newBuilder().build();
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Secrets secrets = Secrets.getSecrets("com/lambda");

    public static final String NEWS_HOST = "api.bing.microsoft.com";
    public static final String NEWS_PATH = "/v7.0/news/search";
    public static final String NEWS_API_KEY = secrets.NEWS_API_KEY;
    public static final HashMap<String, String> NEWS_API_URI_PARAMS = new HashMap<>(Map.of(
            "count", "25",
            "mkt", "en-US",
            "originalImg", "true",
            "setLang", "en-US",
            "sortBy", "Relevance"
    ));

    public static final HashMap<String, String> NEWS_API_URI_HEADERS = new HashMap<>(Map.of("Ocp-Apim-Subscription-Key", NEWS_API_KEY));
    public static final String TRENDING = "TRENDING";

    public HttpRequest prepareRequest(String host, String path, Map<String, String> params,
                                      Map<String, String> headers) {
        String query = "";
        if (params != null) {
            query = params.entrySet().stream()
                    .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
                    .collect(Collectors.joining("&"));
        }

        URI uri;
        try {
            uri = new URI("https", null, host, -1, path, query, null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder().uri(uri);
        if (headers != null) {
            headers.forEach(httpRequestBuilder::header);
        }
        return httpRequestBuilder.build();
    }

    public Mono<City> prepareResponse(String host, String path, Map<String, String> params,
                                      Map<String, String> headers) {
        HttpRequest request = prepareRequest(host, path, params, headers);
        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

//        save data locally

        return Mono.fromFuture(response)
                .map(HttpResponse::body).flatMap(body -> {
                    City city;
                    try {
                        city = objectMapper.readValue(body, City.class);
                    } catch (JsonProcessingException ex) {
                        return Mono.error(new RuntimeException(ex));
                    }
                    return Mono.just(city);
                });
    }

    public Mono<City> fetchNews(City city) {
        String query;
        if(city.getName().equals(TRENDING)){
            NEWS_API_URI_PARAMS.put("category", "us");
            query = "usa news";
        } else {
            query=city.prepareQuery();
        }
        NEWS_API_URI_PARAMS.put("q", query);
        return prepareResponse(NEWS_HOST, NEWS_PATH, NEWS_API_URI_PARAMS, NEWS_API_URI_HEADERS)
                .map(fetchedCity -> {
                    fetchedCity.setName(city.getName());
                    fetchedCity.setState(city.getState());
                    fetchedCity.setUpdateDateToNow();
                    return fetchedCity;
                });
    }
}
