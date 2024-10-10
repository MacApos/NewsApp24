package com.libtest.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(LocalDateTime datePublished) {
        this.datePublished = datePublished;
    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date.replaceAll("\\.\\d+Z$", ""));
    }

//    @Override
//    public int compareTo(Article article) {
//        return parseDate(article.getDatePublished()).compareTo(parseDate(datePublished));
//    }
}

