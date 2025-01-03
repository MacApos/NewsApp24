package com.elasticBeanstalk.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "articles")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article implements Comparable<Article> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 1000)
    private String name;

    @NotNull
    @Column(length = 1000)
    private String url;

    @Column(length = 1000)
    private String image;

    @Column(length = 1000000)
    private String description;

    private LocalDateTime datePublished;

    public Article() {
    }

    @JsonProperty("image")
    private void unpackNested(Map<String, Object> imageJson) {
        image = (String) imageJson.get("contentUrl");
    }

    @JsonProperty("datePublished")
    private void formatDate(String date) {
        datePublished = LocalDateTime.parse(date.replaceAll("[T,Z]|\\.0*", " ").trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public int compareTo(Article article) {
        return article.datePublished.compareTo(datePublished);
    }
}

