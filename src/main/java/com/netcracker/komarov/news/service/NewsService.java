package com.netcracker.komarov.news.service;

import com.netcracker.komarov.news.dao.entity.NewsStatus;
import com.netcracker.komarov.news.service.dto.entity.NewsDTO;
import com.netcracker.komarov.news.service.exception.LogicException;
import com.netcracker.komarov.news.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public interface NewsService {
    Collection<NewsDTO> findAllNewsBySpecification(Map<String, String> params);

    Collection<NewsDTO> findAllNewsByClientId(long clientId);

    NewsDTO findById(long newsId) throws NotFoundException;

    Collection<NewsDTO> findAllNews();

    Collection<NewsDTO> findAllNewsByStatus(NewsStatus newsStatus);

    NewsDTO save(NewsDTO newsDTO, long adminId);

    NewsDTO sendNewsToClient(Collection<Long> clientIds, long newsId) throws NotFoundException, LogicException;

    NewsDTO update(NewsDTO newsDTO) throws NotFoundException;

    NewsDTO findGeneralNewsById(long newsId) throws LogicException;

    void deleteById(long newsId) throws NotFoundException;
}
