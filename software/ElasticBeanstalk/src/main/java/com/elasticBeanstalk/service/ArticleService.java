package com.elasticBeanstalk.service;

import com.elasticBeanstalk.dao.Article;
import com.elasticBeanstalk.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public void saveArticle(Article article) {
        long count = articleRepository.count();
        article.setName(article.getName() + count);
        article.setUrl(article.getUrl() + count);
        article.setContentUrl(article.getContentUrl() + count);
        article.setDescription(article.getDescription() + count);
        articleRepository.save(article);
    }

    public void deleteArticle(){
        articleRepository.deleteAll();
    }

    public void updateArticle(Article article){
        articleRepository.save(article);
    }

}