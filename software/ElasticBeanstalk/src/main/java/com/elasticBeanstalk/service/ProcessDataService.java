package com.elasticBeanstalk.service;

import com.library.dto.City;
import com.library.service.FetchDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ProcessDataService {
    private final FetchDataService fetchDataService;
    private final Validator validator;
    public ResponseStatusException cityNotFound =
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "City not found");
    private final String countryCode = "US";

    @Autowired
    public ProcessDataService(Validator validator, FetchDataService fetchDataService) {
        this.validator = validator;
        this.fetchDataService = fetchDataService;
    }

    @Value("${cityHost}")
    String cityHost;

    @Value("${cityPath}")
    String cityPath;

    @Value("${cityApiKey}")
    public String cityApiKey;

    public Mono<List<City>> fetchCity(String query) {
        String path = cityPath + "/direct";
        Map<String, String> cityApiUriParams = Map.of("appid", cityApiKey, "q", query);
        return fetchDataService.createRequestSpec(cityHost, path, cityApiUriParams, null)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    public Mono<City> validateCity(String name, String state) {
        return Mono.just(new City(name, state))
                // Validation
                .flatMap(initialCity -> {
                    Errors errors = new BeanPropertyBindingResult(initialCity,
                            City.class.getName());
                    validator.validate(initialCity, errors);
                    if (errors.getAllErrors().isEmpty()) {
                        return fetchCity(initialCity.prepareQuery(countryCode));
                    } else {
                        return Mono.error(cityNotFound);
                    }
                })
                // Fetching
                .flatMap(results -> {
                    // Data passed validation but city wasn't found
                    if (results.isEmpty()) {
                        return Mono.error(cityNotFound);
                    } else {
                        return Mono.just(results.get(0));
                    }
                });
    }

}
