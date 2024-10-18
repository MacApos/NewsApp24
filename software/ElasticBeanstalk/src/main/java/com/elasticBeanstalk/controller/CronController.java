package com.elasticBeanstalk.controller;

import com.elasticBeanstalk.dao.Article;
import com.elasticBeanstalk.service.ArticleService;
import org.hibernate.annotations.Comment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
@RestController
public class CronController {
    private final ArticleService articleService;

    public CronController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/update")
    public String updateLastArticle() {
        List<Article> all = articleService.findAll();
        Article last = all.get(all.size() - 1);
//        last.setDatePublished(LocalDateTime.now());
        articleService.updateArticle(last);
        return "updated";
    }
}
