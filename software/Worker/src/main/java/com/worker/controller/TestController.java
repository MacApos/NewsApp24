package com.worker.controller;

import com.worker.dao.Article;
import com.worker.service.ArticleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestController {

    private final ArticleService articleService;

    public TestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping("/")
    public String home() {
        return "ok";
    }

    @RequestMapping("/all")
    public String findAllArticles() {
        List<Article> all = articleService.findAll();
        return all.stream().map(Article::getName).collect(Collectors.joining(", "));
    }

    @RequestMapping("/delete-all")
    public String purge() {
        articleService.deleteArticle();
        return "all deleted";
    }

    @PostMapping("/add")
    public String addArticle() {
        System.out.println("Received cron job request: Updating database...");
//        Article article = new Article("name", "url", "contentUrl", "description");
//        articleService.saveArticle(article);
        return "added";
    }
}
