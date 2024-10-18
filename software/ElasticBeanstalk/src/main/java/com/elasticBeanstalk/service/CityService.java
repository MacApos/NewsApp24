package com.elasticBeanstalk.service;

import com.elasticBeanstalk.dao.News;
import com.elasticBeanstalk.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CityService {
    private final NewsRepository newsRepository;

    public CityService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public News findCity(News news) {
        Optional<News> cityByName = newsRepository.findById(news.getCityName());
        return cityByName.orElse(null);
    }

    public void saveCity(News news) {
        newsRepository.save(news);
    }

}
