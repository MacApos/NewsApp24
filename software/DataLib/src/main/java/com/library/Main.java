package com.library;


import com.library.dto.City;
import com.library.service.DynamoDBService;
import com.library.service.FetchDataService;

import java.time.LocalDateTime;

public class Main {
    private static final FetchDataService fetchDataService = new FetchDataService();
    private static final DynamoDBService dynamoDBService = new DynamoDBService();

    public static void main(String[] args){
//        City city = new City("Ashburn", "Virginia");
//        city.setDateUpdated(String.valueOf(LocalDateTime.now()));
//        fetchDataService.fetchCity(city.prepareQuery());
//        System.out.println();
    }
}
