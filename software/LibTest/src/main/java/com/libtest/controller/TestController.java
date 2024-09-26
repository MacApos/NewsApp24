package com.libtest.controller;

import com.dataProcessingLibrary.dao.Article;
import com.dataProcessingLibrary.service.ArticleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class TestController {
    private final ArticleService articleService;

    public TestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping("/")
    public String testController() {
        Article article = new Article("Name", "Url", "ContentUrl", "Description");
        articleService.saveArticle(article);
        return articleService.findAll().stream().map(Article::getName).collect(Collectors.joining(", "));
    }
}
