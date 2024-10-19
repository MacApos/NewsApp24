package com.elasticBeanstalk.repository;

import com.elasticBeanstalk.dao.News;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    News findByCityNameAndState( String cityName, String state);

}