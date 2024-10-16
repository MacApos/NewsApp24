package com.worker.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name="cities")
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
    @Id
    @Size(min = 3)
    private String name;

    @Size(min = 2)
    private String state;

    @JsonProperty("value")
    @OneToMany
    private ArrayList<Article> articles;
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

}
