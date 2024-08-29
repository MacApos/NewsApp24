package com.elasticBeanstalk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lambda.Secrets;
import com.lambda.Secrets;
import com.lambda.dto.City;
import com.lambda.service.FetchDataService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ProcessDataService {
    private final Validator validator;
    private final FetchDataService fetchDataService = new FetchDataService();
    private final Secrets secrets = Secrets.getSecrets();

    private final String CITY_HOST = "api.openweathermap.org";
    private final String CITY_PATH = "/geo/1.0";
    private final String COUNTRY_CODE = "US";
    private final String CITY_API_KEY = secrets.CITY_API_KEY;
    private final ResponseStatusException CITY_NOT_FOUND =
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "City not found");

    public ProcessDataService(Validator validator) {
        this.validator = validator;
    }

    public Mono<City> fetchCity(String query) {
        String path = CITY_PATH + "/direct";
        Map<String, String> cityApiUriParams = Map.of("appid", CITY_API_KEY, "q", query);
//        return fetchDataService.prepareResponse(CITY_HOST, path, cityApiUriParams, null);

        ObjectMapper objectMapper = new ObjectMapper();
        String mockCity = "[{\"name\":\"New York County\",\"local_names\":{\"te\":\"న్యూయొర్క్\",\"ko\":\"뉴욕\",\"en\":\"New York\",\"gl\":\"Nova York\",\"fr\":\"New York\",\"he\":\"ניו יורק\",\"is\":\"Nýja Jórvík\",\"cs\":\"New York\",\"uk\":\"Нью-Йорк\",\"ca\":\"Nova York\",\"eo\":\"Novjorko\",\"vi\":\"New York\",\"el\":\"Νέα Υόρκη\",\"es\":\"Nueva York\",\"hi\":\"न्यूयॊर्क्\",\"be\":\"Нью-Ёрк\",\"ar\":\"نيويورك\",\"pt\":\"Nova Iorque\",\"oc\":\"Nòva York\",\"zh\":\"纽约/紐約\",\"de\":\"New York\",\"ru\":\"Нью-Йорк\",\"fa\":\"نیویورک\",\"cy\":\"Efrog Newydd\",\"ja\":\"ニューヨーク\",\"it\":\"New York\",\"pl\":\"Nowy Jork\",\"kn\":\"ನ್ಯೂಯೊರ್ಕ್\"},\"lat\":40.7127281,\"lon\":-74.0060152,\"country\":\"US\",\"state\":\"New York\"}]";
        City city;
        try {
            city = objectMapper.readValue(mockCity, City.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Mono.just(city);
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
                        return Mono.error(CITY_NOT_FOUND);
                    }
                })
                // Fetching
                .flatMap(city -> {
                    // Data passed validation but city wasn't found
                    if (city.getName() == null || city.getState() == null) {
                        return Mono.error(CITY_NOT_FOUND);
                    } else {
                        return Mono.just(city);
                    }
                });
    }

}
