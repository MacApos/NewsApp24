package com.libtest.controller;

import com.libtest.dao.Article;
import com.libtest.service.ArticleService;
import com.libtest.service.SqsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.model.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestController {

    private final ArticleService articleService;
    private final SqsService sqsService;
    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    public TestController(ArticleService articleService, SqsService sqsService) {
        this.articleService = articleService;
        this.sqsService = sqsService;
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

    @GetMapping("/update")
    public String updateLastArticle() {
        List<Article> all = articleService.findAll();
        Article last = all.get(all.size() - 1);
        last.setDatePublished(LocalDateTime.now());
        articleService.updateArticle(last);
        logger.info("updated");
        return "updated";
    }

//    @RequestMapping("/sqs")
//    public String getSqsMessage() {
//        return sqsService.receiveMessages()
//                .stream()
//                .map(Message::body)
//                .collect(Collectors.joining(", "));
//    }
}
