package com.elasticBeanstalk.service;

import com.elasticBeanstalk.dao.Article;
import com.elasticBeanstalk.dao.News;
import com.elasticBeanstalk.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAllByNews(News news) {
        return articleRepository.findAllByNewsId(news.getId());
    }
}