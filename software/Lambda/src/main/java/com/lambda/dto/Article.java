package com.lambda.dto;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.LocalDateTime;

@DynamoDbBean
public class Article implements Comparable<Article> {
    private String name;
    private String url;
    private String image;
    private String description;
    private String datePublished;

    public Article() {
    }

    public Article(String name, String url, String image, String description, String datePublished) {
        this.name = name;
        this.url = url;
        this.image = image;
        this.description = description;
        this.datePublished = datePublished;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date.replaceAll("\\.\\d+Z$", ""));
    }

    @Override
    public int compareTo(Article article) {
        return parseDate(datePublished).compareTo(parseDate(article.getDatePublished()));
    }
}

