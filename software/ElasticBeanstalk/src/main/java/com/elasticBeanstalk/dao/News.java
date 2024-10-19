package com.elasticBeanstalk.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

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

    public String prepareQuery() {
        if (state == null) {
            return cityName;
        }
        return String.join(",", List.of(cityName, state));
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
