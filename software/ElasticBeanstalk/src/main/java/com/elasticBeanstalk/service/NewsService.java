package com.elasticBeanstalk.service;

import com.elasticBeanstalk.dao.Article;
import com.elasticBeanstalk.dao.News;
import com.elasticBeanstalk.repository.NewsRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
public class NewsService {
    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> findAll() {
        return newsRepository.findAll();
    }

    public News findNews(News news) {
        return newsRepository.findByCityNameAndState(news.getCityName(), news.getCityName());
    }

    public Mono<News> findMonoNews(News news) {
        return Mono.fromCallable(() -> newsRepository.findByCityNameAndState(news.getCityName(), news.getState()))
                .subscribeOn(Schedulers.boundedElastic());
    }


    public void saveNews(News news) {
        newsRepository.save(news);
    }

    public void deleteNews(News news) {
        newsRepository.delete(news);
    }
}
