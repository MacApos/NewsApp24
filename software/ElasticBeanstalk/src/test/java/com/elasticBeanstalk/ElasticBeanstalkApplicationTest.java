package com.elasticBeanstalk;

import com.elasticBeanstalk.controller.NewsController;
import com.elasticBeanstalk.dao.News;
import com.elasticBeanstalk.repository.NewsRepository;
import com.elasticBeanstalk.service.FetchDataService;
import com.elasticBeanstalk.service.NewsService;
import com.elasticBeanstalk.service.ProcessDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.client.WebClient;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


//@SpringBootTest
@DataJpaTest
@Import(ObjectMapper.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ElasticBeanstalkApplicationTest {
    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class ElasticBeanstalkApplicationTestContextConfiguration {

        @Bean
        public NewsService newsService(NewsRepository newsRepository) {
            return new NewsService(newsRepository);
        }
    }

    @Autowired
    private NewsService newsService;


    @Test
    void springTest() throws Exception {
        String path = "/home/zalman/Documents/JavaProjects/NewsApp24/software/ElasticBeanstalk/src/test/java/com/elasticBeanstalk";
//        News completeNews = jsonToObj(path + "/news-complete.json");
//        News news = newsService.findNews(completeNews);
//        assertEquals(completeNews.getCityName(), news.getCityName());
        List<News> all = newsService.findAll();
        News news = newsService.findNews(new News("New York", "New York"));
        assertNotNull(news);
    }

    News jsonToObj(String path) {
        String json;
        try {
            json = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            return objectMapper.readValue(json, News.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}