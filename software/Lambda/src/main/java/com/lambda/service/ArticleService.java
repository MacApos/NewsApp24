//package com.lambda.service;
//
//import com.lambda.entity.Article;
//import com.lambda.repository.ArticleRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ArticleService {
//    private final ArticleRepository articleRepository;
//
//    public ArticleService(ArticleRepository articleRepository) {
//        this.articleRepository = articleRepository;
//    }
//
//    public String findAll() {
//        return articleRepository.findAll().stream().map(Article::getName).collect(Collectors.joining(", "));
//    }
//}