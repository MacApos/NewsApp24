package com.elasticBeanstalk.controller;

import com.elasticBeanstalk.dao.Article;
import com.elasticBeanstalk.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestController {

    private final ArticleService articleService;
    private final Logger logger = LoggerFactory.getLogger(TestController.class);

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

    @RequestMapping("/add")
    public String addArticle() {
        Article article = new Article("name", "url", "contentUrl", "description");
        articleService.saveArticle(article);
        return article.getName()+"added";
    }

//    @RequestMapping("/city")
//    public String findCity() {
//
//    }

}
