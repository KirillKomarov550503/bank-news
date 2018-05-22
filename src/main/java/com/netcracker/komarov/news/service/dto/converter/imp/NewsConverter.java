package com.netcracker.komarov.news.service.dto.converter.imp;

import com.netcracker.komarov.news.dao.entity.News;
import com.netcracker.komarov.news.dao.entity.NewsStatus;
import com.netcracker.komarov.news.service.dto.converter.Converter;
import com.netcracker.komarov.news.service.dto.entity.NewsDTO;
import org.springframework.stereotype.Component;

@Component
public class NewsConverter implements Converter<NewsDTO, News> {
    @Override
    public NewsDTO convertToDTO(News news) {
        NewsDTO newsDTO = null;
        if (news != null) {
            newsDTO = new NewsDTO();
            newsDTO.setId(news.getId());
            newsDTO.setDate(news.getDate());
            newsDTO.setText(news.getText());
            newsDTO.setTitle(news.getTitle());
            newsDTO.setStatus(news.getNewsStatus().toString());
        }
        return newsDTO;
    }

    @Override
    public News convertToEntity(NewsDTO dto) {
        News news = new News();
        news.setTitle(dto.getTitle());
        news.setText(dto.getText());
        news.setDate(dto.getDate());
        String status = dto.getStatus().toLowerCase();
        if (status.equals("client")) {
            news.setNewsStatus(NewsStatus.CLIENT);
        } else if (status.equals("general")) {
            news.setNewsStatus(NewsStatus.GENERAL);
        }
        return news;
    }
}
