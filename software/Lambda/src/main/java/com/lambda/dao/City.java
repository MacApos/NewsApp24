package com.lambda.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
    @NotNull
    @Size(min = 3)
    private String name;

    @Size(min = 2)
    private String state;

    @JsonProperty("value")
    private ArrayList<Article> articles = new ArrayList<>();
    private LocalDateTime updateDate;

    @JsonProperty("local_names")
    private void unpackNested(Map<String,Object> localNamesJson) {
        name = (String) localNamesJson.get("en");
    }

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
        this.articles = new ArrayList<>(values);
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

}
