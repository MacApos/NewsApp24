package com.library.dto;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public class Article {
    private String name;
    private String url;
    private String contentUrl;
    private String description;
    private String datePublished;

    public Article() {
    }

    public Article(String name, String url, String contentUrl, String description, String datePublished) {
        this.name = name;
        this.url = url;
        this.contentUrl = contentUrl;
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

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }
}

