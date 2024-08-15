package com.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.library.deserializer.CityDeserializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DynamoDbBean
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = CityDeserializer.class)
public class City {
    @NotNull
    @Size(min = 3)
    private String name;

    @NotNull
    @Size(min = 2)
    private String state;
    private List<Article> articles = new ArrayList<>();

    public City() {
    }

    public City(String name, String state) {
        this.name = name;
        this.state = state;
    }

    public City(String name, String state, ArrayList<Article> articles) {
        this.name = name;
        this.state = state;
        this.articles = articles;
    }

    public String prepareQuery(String... params) {
        ArrayList<String> queryParams = new ArrayList<>(List.of(name, state));
        Collections.addAll(queryParams, params);
        return String.join(",", queryParams);
    }

    public void addArticle(Article article){
        articles.add(article);
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
