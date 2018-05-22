package com.netcracker.komarov.news.service;

import com.netcracker.komarov.news.dao.entity.NewsStatus;
import com.netcracker.komarov.news.service.dto.entity.NewsDTO;
import com.netcracker.komarov.news.service.exception.LogicException;
import com.netcracker.komarov.news.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface NewsService {
    Collection<NewsDTO> getAllClientNewsById(long clientId) throws NotFoundException;

    NewsDTO findById(long newsId) throws NotFoundException;

    Collection<NewsDTO> getAllNews();

    Collection<NewsDTO> getAllNewsByStatus(NewsStatus newsStatus);

    NewsDTO addNews(NewsDTO newsDTO, long adminId);

    NewsDTO addClientNews(Collection<Long> clientIds, long newsId) throws NotFoundException, LogicException;

    NewsDTO update(NewsDTO newsDTO) throws NotFoundException;

    NewsDTO findGeneralNewsById(long newsId) throws NotFoundException, LogicException;

    void deleteById(long newsId) throws NotFoundException;
}
