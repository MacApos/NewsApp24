package com.lambda.event;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.library.dto.City;

import java.util.Map;

public class SaveDataHandler implements RequestHandler<Map<String, String>, String> {

    @Override
    public String handleRequest(Map<String, String> stringStringMap, Context context) {
        City city = new City("test", "city");
        return city.prepareQuery();
    }
}