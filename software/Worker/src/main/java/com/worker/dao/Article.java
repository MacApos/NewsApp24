package com.worker.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name="articles")
public class Article {
    @Id
    private String name;

    @NotNull
    private String url;

    private String contentUrl;
    private String description;

    @Column(columnDefinition="datetime default now()")
    private LocalDateTime datePublished = LocalDateTime.now();

    public Article() {
    }

    public Article(String name, String url, String contentUrl, String description) {
        this.name = name;
        this.url = url;
        this.contentUrl = contentUrl;
        this.description = description;
    }

    @JsonProperty("image")
    private void unpackNested(Map<String,Object> image) {
        contentUrl = (String) image.get("contentUrl");
    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date.replaceAll("\\.\\d+Z$", ""));
    }

//    @Override
//    public int compareTo(Article article) {
//        return parseDate(article.getDatePublished()).compareTo(parseDate(datePublished));
//    }
}

