package com.library.service;

import com.library.dto.Answer;
import com.library.dto.City;
import com.library.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class FetchDataService {
    private final WebClient webClient;
    private final Map<String, String> newsApiUriHeaders;
    private final Map<String, String> newsApiUriParams;
    private final CityMapper cityMapper;

    public FetchDataService(WebClient webClient, Map<String, String> newsApiUriParams,
                            Map<String, String> newsApiUriHeaders, CityMapper cityMapper) {
        this.webClient = webClient;
        this.newsApiUriHeaders = newsApiUriHeaders;
        this.newsApiUriParams = newsApiUriParams;
        this.cityMapper = cityMapper;
    }

    @Value("${newsHost}")
    String newsHost;

    @Value("${newsPath}")
    String newsPath;

    public WebClient.RequestHeadersSpec<?> createRequestSpec(String host, String path,
                                                             Map<String, String> params, Map<String, String> headers) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(host)
                .path(path);
        if (params != null) {
            params.forEach(uriComponentsBuilder::queryParam);
        }
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = webClient
                .get()
                .uri(uriComponentsBuilder.build().toUri());
        if (headers != null) {
            headers.forEach(requestHeadersSpec::header);
        }
        return requestHeadersSpec;
    }

    public Mono<City> fetchNews(String param) {
        newsApiUriParams.put("q", param);
        Mono<Answer> answerMono = createRequestSpec(newsHost, newsPath, newsApiUriParams, newsApiUriHeaders)
                .retrieve()
                .bodyToMono(Answer.class);
        return answerMono.map(cityMapper::answerToCity);
    }

}
