package com.elasticBeanstalk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.dto.City;
import com.library.service.FetchDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.library.service.FetchDataService.*;

@Service
public class ProcessDataService {
    private final FetchDataService fetchDataService = new FetchDataService();
    private final Validator validator;
    private final WebClient webClient;
    public ResponseStatusException cityNotFound =
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "City not found");
    private final String COUNTRY_CODE = "US";

    public ProcessDataService(Validator validator, WebClient webClient) {
        this.validator = validator;
        this.webClient = webClient;
    }

    @Value("${cityHost}")
    String CITY_HOST;

    @Value("${cityPath}")
    String CITY_PATH;

    @Value("${cityApiKey}")
    public String CITY_API_KEY;

    public Mono<Object> fetchCity(String query) throws JsonProcessingException {
        String path = CITY_PATH + "/direct";
        Map<String, String> cityApiUriParams = Map.of("appid", CITY_API_KEY, "q", query);
        URI uri = fetchDataService.createUri(CITY_HOST, path, cityApiUriParams);

        String json = "{\"name\":\"New York County\",\"local_names\":{\"cs\":\"New York\",\"fa\":\"نیویورک\",\"cy\":\"Efrog Newydd\",\"it\":\"New York\",\"pl\":\"Nowy Jork\",\"is\":\"Nýja Jórvík\",\"eo\":\"Novjorko\",\"vi\":\"New York\",\"uk\":\"Нью-Йорк\",\"es\":\"Nueva York\",\"ca\":\"Nova York\",\"te\":\"న్యూయొర్క్\",\"be\":\"Нью-Ёрк\",\"ja\":\"ニューヨーク\",\"oc\":\"Nòva York\",\"hi\":\"न्यूयॊर्क्\",\"ar\":\"نيويورك\",\"zh\":\"纽约/紐約\",\"en\":\"New York\",\"gl\":\"Nova York\",\"kn\":\"ನ್ಯೂಯೊರ್ಕ್\",\"el\":\"Νέα Υόρκη\",\"ko\":\"뉴욕\",\"he\":\"ניו יורק\",\"pt\":\"Nova Iorque\",\"fr\":\"New York\",\"ru\":\"Нью-Йорк\",\"de\":\"New York\"},\"lat\":40.7127281,\"lon\":-74.0060152,\"country\":\"US\",\"state\":\"New York\"}";
        City city = objectMapper.readValue(json, City.class);
        Mono<Object> listMono = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Object.class);
        return Mono.just(city);
    }

    public Mono<Object> validateCity(String name, String state) {
        return Mono.just(new City(name, state))
                // Validation
                .flatMap(initialCity -> {
                    Errors errors = new BeanPropertyBindingResult(initialCity,
                            City.class.getName());
                    validator.validate(initialCity, errors);
                    if (errors.getAllErrors().isEmpty()) {
                        try {
                            return fetchCity(initialCity.prepareQuery(COUNTRY_CODE));
                        } catch (JsonProcessingException e) {
                            return Mono.error(new RuntimeException(e));
                        }
                    } else {
                        return Mono.error(cityNotFound);
                    }
                });
                // Fetching
//                .flatMap(results -> {
//                    // Data passed validation but city wasn't found
//                    if (results.isEmpty()) {
//                        return Mono.error(cityNotFound);
//                    } else {
//                        return Mono.just(results.get(0));
//                    }
//                });
    }

}
