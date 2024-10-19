package com.elasticBeanstalk.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "news")
@JsonIgnoreProperties(ignoreUnknown = true)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3)
    @JsonProperty("name")
    private String cityName;

    @Size(min = 2)
    private String state;

    @JsonProperty("value")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Article> articles;

    @Column(columnDefinition = "datetime default now()")
    private LocalDateTime updateDate = LocalDateTime.now();

    @JsonProperty("local_names")
    private void unpackNested(Map<String, Object> localNamesJson) {
        cityName = (String) localNamesJson.get("en");
    }

    public News() {
    }

    public News(String cityName, String state) {
        this.cityName = cityName;
        this.state = state;
    }

    public String prepareQuery(String... params) {
        int paramsLen = params.length;
        if (state == null && paramsLen == 0) {
            return cityName;
        }
        ArrayList<String> queryParams = new ArrayList<>(List.of(cityName));
        if (state != null) {
            queryParams.add(state);
        }
        Collections.addAll(queryParams, params);
        return String.join(",", queryParams);
    }

//    public void addArticle(Article article) {
//        articles.add(article);
//    }
//
//    public void addArticles(List<Article> articles) {
//        this.articles.addAll(articles);
//    }
//
//    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
//        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
//        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
//    }
//
//    public void sortArticles() {
//        if (!articles.isEmpty()) {
//            articles = articles.stream().filter(distinctByKey(Article::getName)).sorted()
//                    .limit(Math.min(20, articles.size())).collect(Collectors.toCollection(ArrayList::new));
//        }
//    }
//
//    public void setUpdateDateToNow() {
//        updateDate = LocalDateTime.now();
//    }
}
