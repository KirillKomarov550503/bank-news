package com.netcracker.komarov.news.dao.repository;

import com.netcracker.komarov.news.dao.entity.News;
import com.netcracker.komarov.news.dao.entity.NewsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    Collection<News> findNewsByAdminId(long adminId);

    Collection<News> findNewsByNewsStatus(NewsStatus newsStatus);
}
