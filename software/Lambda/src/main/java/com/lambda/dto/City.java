package com.lambda.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lambda.deserializer.CityDeserializer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@DynamoDbBean
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = CityDeserializer.class)
public class City {
    @NotNull
    @Size(min = 3)
    private String name;

    @Size(min = 2)
    private String state;

    private ArrayList<Article> articles  = new ArrayList<>();
    private LocalDateTime updateDate;

    public City() {
    }

    public City(String name, String state) {
        this.name = name;
        this.state = state;
    }

    public String prepareQuery(String... params) {
        int paramsLen = params.length;
        if (state == null && paramsLen == 0) {
            return name;
        }
        ArrayList<String> queryParams = new ArrayList<>(List.of(name));
        if (state != null) {
            queryParams.add(state);
        }
        Collections.addAll(queryParams, params);
        return String.join(",", queryParams);
    }

    public void addArticle(Article article) {
        articles.add(article);
    }

    public void addArticles(List<Article> articles) {
        this.articles.addAll(articles);
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public void sortArticles() {
        if (!articles.isEmpty()) {
            articles = articles.stream().filter(distinctByKey(Article::getName)).sorted()
                    .limit(Math.min(20, articles.size())).collect(Collectors.toCollection(ArrayList::new));
        }
    }

    public void setUpdateDateToNow() {
        updateDate = LocalDateTime.now();
    }

    @DynamoDbPartitionKey
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDbSortKey
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
        this.articles = new ArrayList<>(values);
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

}
