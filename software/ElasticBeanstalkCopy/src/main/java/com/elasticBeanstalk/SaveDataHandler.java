package com.elasticBeanstalk;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.library.dto.City;
import com.library.service.FetchDataService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SaveDataHandler implements RequestHandler<Map<String, String>, String> {
    private final FetchDataService fetchDataService;

    public SaveDataHandler(FetchDataService fetchDataService) {
        this.fetchDataService = fetchDataService;
    }

    @Override
    public String handleRequest(Map<String, String> stringStringMap, Context context) {
        City city = new City("test", "city");
        return city.prepareQuery();
    }
}