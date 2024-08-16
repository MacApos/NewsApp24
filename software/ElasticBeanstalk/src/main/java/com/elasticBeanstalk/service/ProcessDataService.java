package com.elasticBeanstalk.service;

import com.library.dto.City;
import com.library.service.FetchDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

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

    public Mono<City> fetchCity(String query) {
        String path = CITY_PATH + "/direct";
        Map<String, String> cityApiUriParams = Map.of("appid", CITY_API_KEY, "q", query);
        String mockCity = "[{\"name\":\"New York County\",\"local_names\":{\"te\":\"న్యూయొర్క్\",\"ko\":\"뉴욕\",\"en\":\"New York\",\"gl\":\"Nova York\",\"fr\":\"New York\",\"he\":\"ניו יורק\",\"is\":\"Nýja Jórvík\",\"cs\":\"New York\",\"uk\":\"Нью-Йорк\",\"ca\":\"Nova York\",\"eo\":\"Novjorko\",\"vi\":\"New York\",\"el\":\"Νέα Υόρκη\",\"es\":\"Nueva York\",\"hi\":\"न्यूयॊर्क्\",\"be\":\"Нью-Ёрк\",\"ar\":\"نيويورك\",\"pt\":\"Nova Iorque\",\"oc\":\"Nòva York\",\"zh\":\"纽约/紐約\",\"de\":\"New York\",\"ru\":\"Нью-Йорк\",\"fa\":\"نیویورک\",\"cy\":\"Efrog Newydd\",\"ja\":\"ニューヨーク\",\"it\":\"New York\",\"pl\":\"Nowy Jork\",\"kn\":\"ನ್ಯೂಯೊರ್ಕ್\"},\"lat\":40.7127281,\"lon\":-74.0060152,\"country\":\"US\",\"state\":\"New York\"}]";
        return fetchDataService.prepareResponse(CITY_HOST, path, cityApiUriParams, null, mockCity);
    }

    public Mono<City> validateCity(String name, String state) {
        return Mono.just(new City(name, state))
                // Validation
                .flatMap(initialCity -> {
                    Errors errors = new BeanPropertyBindingResult(initialCity,
                            City.class.getName());
                    validator.validate(initialCity, errors);
                    if (errors.getAllErrors().isEmpty()) {
                        return fetchCity(initialCity.prepareQuery(COUNTRY_CODE));
                    } else {
                        return Mono.error(cityNotFound);
                    }
                })
                // Fetching
                .flatMap(city -> {
                    // Data passed validation but city wasn't found
                    if (city.getName() == null || city.getState() == null) {
                        return Mono.error(cityNotFound);
                    } else {
                        return Mono.just(city);
                    }
                });
    }

}
