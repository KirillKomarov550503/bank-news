package com.netcracker.komarov.news.dao.repository;

import com.netcracker.komarov.news.dao.entity.ClientNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientNewsRepository extends JpaRepository<ClientNews, Long> {
    ClientNews findClientNewsByClientIdAndAndNewsId(long clientId, long newsId);
}

