package com.elasticBeanstalk.service;

import com.elasticBeanstalk.dao.Article;
import com.elasticBeanstalk.dao.News;
import com.elasticBeanstalk.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    private final NewsRepository newsRepository;
    private final ArticleService articleService;

    public NewsService(NewsRepository newsRepository, ArticleService articleService) {
        this.newsRepository = newsRepository;
        this.articleService = articleService;
    }

    public News findNews(News news) {
        News byCityNameAndState = newsRepository.findByCityNameAndState(news.getCityName(), news.getCityName());
        if (byCityNameAndState == null) {
            return null;
        }
        List<Article> all = articleService.findAll();
        all.forEach(System.out::println);
        byCityNameAndState.setArticles(all);
        return byCityNameAndState;
    }

    public News saveNews(News news) {
        return newsRepository.saveAndFlush(news);
    }

}
