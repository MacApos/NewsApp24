package com.elasticBeanstalk.repository;

import com.elasticBeanstalk.dao.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, String> {
    News findByCityNameAndAndState(String cityName, String state);
}
