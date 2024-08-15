package com.library;


import com.library.service.FetchDataService;

import java.util.concurrent.ExecutionException;

public class Main {
    private static final FetchDataService fetchDataService = new FetchDataService();

    public static void main(String[] args){
        fetchDataService.fetchNews("hemingford,ne");
        System.out.println();
    }
}
