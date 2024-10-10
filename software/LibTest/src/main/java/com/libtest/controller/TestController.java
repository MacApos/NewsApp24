package com.libtest.controller;

//import com.dataProcessingLibrary.dao.Article;
//import com.dataProcessingLibrary.service.ArticleService;

import com.dataProcessingLibrary.dao.Article;
import com.dataProcessingLibrary.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TestController {

    private final ArticleService articleService;

    public TestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("all")
    @ResponseBody
    public String findAllArticles() {
        List<Article> all = articleService.findAll();
        return all.stream().map(Article::getName).collect(Collectors.joining(", "));
    }

    @RequestMapping("/delete-all")
    @ResponseBody
    public String purge() {
        Article article = new Article("name0", "url0", "contentUrl0", "description0");
        articleService.deleteArticle(article);
        return "all deleted";
    }

    @GetMapping("/add")
    public String addArticle() {
        Article article = new Article("name", "url", "contentUrl", "description");
        articleService.saveArticle(article);
        return "redirect:/all";
    }
}
