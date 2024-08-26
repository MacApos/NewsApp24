package com.lambda.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.Secrets;
import com.lambda.dto.City;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FetchDataService {
    public static final HttpClient httpClient = HttpClient.newBuilder().build();
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final Secrets secrets = Secrets.getSecret();

    public static final String NEWS_HOST = "api.bing.microsoft.com";
    public static final String NEWS_PATH = "/v7.0/news/search";
    public static final String NEWS_API_KEY = secrets.NEWS_API_KEY;
    public static final HashMap<String, String> NEWS_API_URI_PARAMS = new HashMap<>(Map.of("count", "20",
            "mkt", "en-US",
            "setLang", "en",
            "sortBy", "date",
            "originalImg", "true"));
    public static final HashMap<String, String> NEWS_API_URI_HEADERS = new HashMap<>(Map.of("Ocp-Apim-Subscription-Key", NEWS_API_KEY));

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
        return Mono.fromFuture(httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
                .map(HttpResponse::body)
                .handle((body, sink) -> {
                    City city;
                    try {
                        city = objectMapper.readValue(body, City.class);
                    } catch (JsonProcessingException ex) {
                        sink.error(new RuntimeException(ex));
                        return;
                    }
                    sink.next(city);
                });
    }

    public Mono<City> fetchCity(City city) {
        LinkedHashMap<String, String> qParam = new LinkedHashMap<>(Map.of("q", city.prepareQuery()));
        qParam.putAll(NEWS_API_URI_PARAMS);
        return prepareResponse(NEWS_HOST, NEWS_PATH, qParam, NEWS_API_URI_HEADERS)
                .doOnNext(City::setUpdateDateToNow);
    }

    public  Mono<City> mockFetchCity(City city, Map<String, String> newsMap) {
        String news = newsMap.get(city.getName());
        City newCity;
        if (news == null) {
            newCity = new City();
        } else {
            try {
                newCity = objectMapper.readValue(news, City.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return Mono.just(newCity)
                .doOnNext(City::setUpdateDateToNow);
    }


}
