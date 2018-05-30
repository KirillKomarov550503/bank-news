package com.netcracker.komarov.news.service.impl;

import com.netcracker.komarov.news.dao.entity.ClientNews;
import com.netcracker.komarov.news.dao.entity.News;
import com.netcracker.komarov.news.dao.entity.NewsStatus;
import com.netcracker.komarov.news.dao.repository.ClientNewsRepository;
import com.netcracker.komarov.news.dao.repository.NewsRepository;
import com.netcracker.komarov.news.dao.specification.NewsSpecification;
import com.netcracker.komarov.news.service.NewsService;
import com.netcracker.komarov.news.service.dto.converter.imp.NewsConverter;
import com.netcracker.komarov.news.service.dto.entity.NewsDTO;
import com.netcracker.komarov.news.service.exception.LogicException;
import com.netcracker.komarov.news.service.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {
    private NewsRepository newsRepository;
    private NewsConverter newsConverter;
    private ClientNewsRepository clientNewsRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsServiceImpl.class);

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, NewsConverter newsConverter,
                           ClientNewsRepository clientNewsRepository) {
        this.newsRepository = newsRepository;
        this.newsConverter = newsConverter;
        this.clientNewsRepository = clientNewsRepository;
    }

    private boolean isAbsentInDatabase(long clientId, long newsId) {
        ClientNews clientNews = clientNewsRepository.findClientNewsByClientIdAndAndNewsId(clientId, newsId);
        return clientNews == null;
    }

    private Collection<NewsDTO> convertCollection(Collection<News> newsCollection) {
        return newsCollection.stream()
                .map(news -> newsConverter.convertToDTO(news))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Collection<NewsDTO> findAllNews() {
        LOGGER.info("Return all news");
        return convertCollection(newsRepository.findAll());
    }

    @Transactional
    @Override
    public Collection<NewsDTO> findAllNewsByClientId(long clientId) throws NotFoundException {
        List<News> resultCollection = clientNewsRepository.findAll()
                .stream()
                .filter(clientNews -> clientNews.getClientId() == clientId
                        || clientNews.getClientId() == 0)
                .map(clientNews -> newsRepository.findById(clientNews.getNewsId()).get())
                .collect(Collectors.toList());
        LOGGER.info("Return all client news by client ID: " + clientId);
        return convertCollection(resultCollection);
    }

    @Transactional
    @Override
    public NewsDTO findById(long newsId) throws NotFoundException {
        Optional<News> optional = newsRepository.findById(newsId);
        News news;
        if (optional.isPresent()) {
            news = optional.get();
            LOGGER.info("Return client news by ID: " + newsId);
        } else {
            String error = "There is no such news in database with ID: " + newsId;
            LOGGER.error(error);
            throw new NotFoundException(error);
        }
        return newsConverter.convertToDTO(news);
    }

    @Transactional
    @Override
    public NewsDTO save(NewsDTO newsDTO, long adminId) {
        News news = newsConverter.convertToEntity(newsDTO);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        news.setDate(simpleDateFormat.format(new Date()));
        News temp;
        news.setAdminId(adminId);
        temp = newsRepository.save(news);
        LOGGER.info("Add new news to database with ID: " + temp.getId());
        return newsConverter.convertToDTO(temp);
    }

    @Transactional
    @Override
    public Collection<NewsDTO> findAllNewsByStatus(NewsStatus newsStatus) {
        LOGGER.info("Return all news by status: " + newsStatus);
        return convertCollection(newsRepository.findNewsByNewsStatus(newsStatus));
    }

    @Transactional
    @Override
    public NewsDTO sendNewsToClient(Collection<Long> clientIds, long newsId) throws NotFoundException, LogicException {
        Optional<News> optionalNews = newsRepository.findById(newsId);
        News news;
        if (optionalNews.isPresent()) {
            news = optionalNews.get();
            if (news.getNewsStatus().equals(NewsStatus.GENERAL)) {
                String error = "Try to send general news to clients";
                LOGGER.error(error);
                throw new LogicException(error);
            }
            if (isAbsentInDatabase(0L, newsId)) {
                if (clientIds.isEmpty()) {
                    clientNewsRepository.save(new ClientNews(0L, newsId));
                } else {
                    clientIds.stream()
                            .filter(clientId -> isAbsentInDatabase(clientId, newsId))
                            .forEach(clientId -> clientNewsRepository.save(new ClientNews(clientId, newsId)));
                }
            }
            LOGGER.info("Send new to clients");
        } else {
            String error = "There is no such news in database with ID: " + newsId;
            LOGGER.error(error);
            throw new NotFoundException(error);
        }
        return newsConverter.convertToDTO(news);
    }

    @Transactional
    @Override
    public NewsDTO update(NewsDTO newsDTO) throws NotFoundException {
        News newNews = newsConverter.convertToEntity(newsDTO);
        Optional<News> optionalNews = newsRepository.findById(newsDTO.getId());
        News resNews;
        if (optionalNews.isPresent()) {
            News oldNews = optionalNews.get();
            oldNews.setTitle(newNews.getTitle());
            oldNews.setText(newNews.getText());
            resNews = newsRepository.saveAndFlush(oldNews);
            LOGGER.info("News with ID " + resNews.getId() + " was edited by admin");
        } else {
            String error = "There is no such news in database with ID " + newsDTO.getId();
            LOGGER.error(error);
            throw new NotFoundException(error);
        }
        return newsConverter.convertToDTO(resNews);
    }

    @Transactional
    @Override
    public NewsDTO findGeneralNewsById(long newsId) throws NotFoundException, LogicException {
        Optional<News> optionalNews = newsRepository.findById(newsId);
        News news;
        if (optionalNews.isPresent()) {
            news = optionalNews.get();
            if (news.getNewsStatus().equals(NewsStatus.CLIENT)) {
                String error = "You do not have access to this news";
                LOGGER.error(error);
                throw new LogicException(error);
            }
        } else {
            String error = "No such news with ID " + newsId;
            LOGGER.error(error);
            throw new NotFoundException(error);
        }
        LOGGER.info("Return news with ID " + newsId);
        return newsConverter.convertToDTO(news);
    }

    @Transactional
    @Override
    public void deleteById(long newsId) throws NotFoundException {
        Optional<News> optionalNews = newsRepository.findById(newsId);
        if (optionalNews.isPresent()) {
            News news = optionalNews.get();
            if (news.getNewsStatus().equals(NewsStatus.CLIENT)) {
                clientNewsRepository.deleteClientNewsByNewsId(newsId);
            }
            newsRepository.deleteById(newsId);
            LOGGER.info("Delete news with ID " + newsId);
        } else {
            String error = "No such news with ID " + newsId;
            LOGGER.error(error);
            throw new NotFoundException(error);
        }
    }

    @Transactional
    @Override
    public Collection<NewsDTO> findAllNewsBySpecification(Map<String, String> params) {
        Collection<News> newsCollection = newsRepository.findAll(NewsSpecification.findByStatus(params));
        LOGGER.info("Return all news by specification");
        return convertCollection(newsCollection);
    }
}
