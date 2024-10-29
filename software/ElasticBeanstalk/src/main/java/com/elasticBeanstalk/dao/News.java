package com.elasticBeanstalk.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import validator.State;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

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

    @State
    private String state;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Article> articles;

    @Column(columnDefinition = "datetime default now()")
    private LocalDateTime updateDate = LocalDateTime.now();

    @JsonProperty("local_names")
    private void unpackNested(Map<String, Object> localNamesJson) {
        cityName = (String) localNamesJson.get("en");
    }

    //Serialization
    @JsonProperty("articles")
    public List<Article> getArticles() {
        return articles;
    }

    //Deserialization
    @JsonProperty("value")
    public void setArticles(List<Article> articles) {
        this.articles = articles;
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

    public <T> Predicate<T> distinctByProperty(Function<? super T, ?> propertyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(propertyExtractor.apply(t));
    }

    public void sortArticles() {
        if (!articles.isEmpty()) {
            articles = articles.stream()
                    .filter(distinctByProperty(Article::getName))
                    .limit(20)
                    .sorted()
                    .toList();
        }
    }
}
