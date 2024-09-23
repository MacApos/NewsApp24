package com.dataProcessLibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dataProcessLibrary.dao.City;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class FetchDataService {
    public static final HttpClient httpClient = HttpClient.newBuilder().build();
    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final SecretsService SECRETS_SERVICE = SecretsService.getSecrets();

    public static final String NEWS_HOST = "api.bing.microsoft.com";
    public static final String NEWS_PATH = "/v7.0/news/search";
    public static final String NEWS_API_KEY = SECRETS_SERVICE.NEWS_API_KEY;
    public static final HashMap<String, String> NEWS_API_URI_PARAMS = new HashMap<>(Map.of(
            "count", "25",
            "mkt", "en-US",
            "originalImg", "true",
            "setLang", "en-US",
            "sortBy", "Relevance"
    ));

    public static final HashMap<String, String> NEWS_API_URI_HEADERS = new HashMap<>(Map.of("Ocp-Apim-Subscription-Key", NEWS_API_KEY));
    public static final String TRENDING = "TRENDING";

    private final WebClient webClient;

    public FetchDataService(WebClient webClient) {
        this.webClient = webClient;
    }

    public HttpRequest prepareRequest(String host, String path, Map<String, String> params,
                                      Map<String, String> headers) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(path);

        String query = "";
        if (params != null) {
            params.forEach(uriComponentsBuilder::queryParam);
        }

        URI uri1 = uriComponentsBuilder.build().toUri();
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient.get().uri(uri1);

        URI uri;
        try {
            uri = new URI("https", null, host, -1, path, query, null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder().uri(uri);
        if (headers != null) {
            headers.forEach(httpRequestBuilder::header);
            headers.forEach(requestHeadersSpec::header);
        }
        Mono<City> objectMono = requestHeadersSpec.retrieve().bodyToMono(City.class);
        return httpRequestBuilder.build();
    }

    public Mono<City> prepareResponse(String host, String path, Map<String, String> params,
                                      Map<String, String> headers) {
        HttpRequest request = prepareRequest(host, path, params, headers);
        CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());

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
        if (city.getName().equals(TRENDING)) {
            NEWS_API_URI_PARAMS.put("category", "us");
            query = "usa news";
        } else {
            query = city.prepareQuery();
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
