package com.data.repository;

import com.data.entity.News;
import com.data.enums.NewsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("""
        SELECT n FROM News n
        WHERE (:title IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', :title, '%')))
        AND (:poster IS NULL OR LOWER(n.postedBy.username) LIKE LOWER(CONCAT('%', :poster, '%')))
    """)
    Page<News> searchNews(
            @Param("title") String title,
            @Param("poster") String poster,
            Pageable pageable
    );

    List<News> findByNewsCategoryOrderByPostedAtDesc(NewsCategory newsCategory);

    List<News> findAllByOrderByPostedAtDesc();
}