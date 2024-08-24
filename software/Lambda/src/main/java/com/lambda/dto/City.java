package com.lambda.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lambda.deserializer.CityDeserializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;
import java.util.*;

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
    private String updateDate;

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

    public void addArticle(Article article) {
        articles.add(article);
    }

    public void updateCity(City city) {
        List<Article> newArticles = city.getArticles();
        if (articles.isEmpty()) {
            articles = city.getArticles();
            return;
        }
        TreeSet<Article> currentArticles = new TreeSet<>(articles);
        currentArticles.addAll(newArticles);
        articles = currentArticles.stream().limit(20).toList();
    }

    public void setUpdateDateToNow() {
        updateDate = String.valueOf(LocalDateTime.now());
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

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

}
