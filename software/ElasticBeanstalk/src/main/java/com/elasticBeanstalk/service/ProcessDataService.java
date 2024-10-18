package com.elasticBeanstalk.service;

import com.elasticBeanstalk.dao.News;
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
    private final FetchDataService fetchDataService;
    private final SecretsService secretsService = SecretsService.getSecrets();

    private final String CITY_HOST = "api.openweathermap.org";
    private final String CITY_PATH = "/geo/1.0/direct";
    private final String COUNTRY_CODE = "US";
    private final String CITY_API_KEY = secretsService.CITY_API_KEY;
    private final ResponseStatusException CITY_NOT_FOUND =
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "City not found");

    public ProcessDataService(FetchDataService fetchDataService, Validator validator) {
        this.fetchDataService = fetchDataService;
        this.validator = validator;
    }

    public Mono<News> fetchCity(String query) {
        Map<String, String> cityApiUriParams = Map.of("appid", CITY_API_KEY, "q", query);
        Mono<News> cityMono = fetchDataService.prepareResponse(CITY_HOST, CITY_PATH, cityApiUriParams, null, false);
        return cityMono;
    }

    public Mono<News> validateCity(News news) {
        return Mono.just(news)
                // Validation
                .flatMap(initialCity -> {
                    Errors errors = new BeanPropertyBindingResult(initialCity,
                            News.class.getName());
                    validator.validate(initialCity, errors);
                    if (errors.getAllErrors().isEmpty()) {
                        return fetchCity(initialCity.prepareQuery(COUNTRY_CODE));
                    }
                    return Mono.error(CITY_NOT_FOUND);
                })
                // Fetching
                .flatMap(validCity -> {
                    // Data passed validation but city wasn't found
                    if (validCity.getCityName() == null) {
                        return Mono.error(CITY_NOT_FOUND);
                    }
                    return Mono.just(validCity);
                });
    }
}
