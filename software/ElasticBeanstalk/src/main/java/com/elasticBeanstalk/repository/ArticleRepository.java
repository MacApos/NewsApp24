package com.elasticBeanstalk.repository;

import com.elasticBeanstalk.dao.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value = """
                select a.*
                from news_articles as na
                    left join articles a on a.id = na.articles_id where na.news_id=?;
            """, nativeQuery = true)
    List<Article> findAllByNewsId(Long id);
}