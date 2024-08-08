package com.newsapp24.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.*;

@Component
@DynamoDbBean
public class City {
    @NotNull
    @Size(min = 3)
    private String name;

    @NotNull
    @Size(min = 2)
    private String state;
    private List<Article> articles;

    public City() {
    }

    public City(String name, String state) {
        this.name = name;
        this.state = state;
    }

    public City(String name, String state, List<Article> articles) {
        this.name = name;
        this.state = state;
        this.articles = articles;
    }

    public String prepareQuery( String... params) {
        ArrayList<String> queryParams = new ArrayList<>(List.of(name, state));
        Collections.addAll(queryParams, params);
        return String.join(",", queryParams);
    }

    @DynamoDbPartitionKey
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> values) {
        this.articles = values;
    }


}
