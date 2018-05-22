package com.netcracker.komarov.news.controller;

import com.netcracker.komarov.news.dao.entity.NewsStatus;
import com.netcracker.komarov.news.service.NewsService;
import com.netcracker.komarov.news.service.dto.entity.NewsDTO;
import com.netcracker.komarov.news.service.exception.LogicException;
import com.netcracker.komarov.news.service.exception.NotFoundException;
import com.netcracker.komarov.news.service.json.NewsJson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/bank/v1")
public class NewsController {
    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @ApiOperation(value = "Creation of new news")
    @RequestMapping(value = "/admins/{adminId}/news", method = RequestMethod.POST)
    public ResponseEntity add(@PathVariable long adminId, @RequestBody NewsDTO newsDTO) {
        ResponseEntity responseEntity;
        NewsDTO dto = newsService.addNews(newsDTO, adminId);
        responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(new NewsJson(dto));
        return responseEntity;
    }

    @ApiOperation(value = "Selecting all client news by client ID")
    @RequestMapping(value = "/client/{clientId}/news", method = RequestMethod.GET)
    public ResponseEntity getAllClientNewsById(@PathVariable long clientId) {
        ResponseEntity responseEntity;
        try {
            Collection<NewsDTO> dtos = newsService.getAllClientNewsById(clientId);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(convertToArray(dtos));
        } catch (NotFoundException e) {
            responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Selecting all general news")
    @RequestMapping(value = "/news", method = RequestMethod.GET)
    public ResponseEntity findAllGeneralNews() {
        Collection<NewsDTO> dtos = newsService.getAllNewsByStatus(NewsStatus.GENERAL);
        return ResponseEntity.status(HttpStatus.OK).body(convertToArray(dtos));
    }

    @ApiOperation(value = "Selecting news by ID")
    @RequestMapping(value = "/admins/news/{newsId}", method = RequestMethod.GET)
    public ResponseEntity findById(@PathVariable long newsId) {
        ResponseEntity responseEntity;
        try {
            NewsDTO dto = newsService.findById(newsId);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(new NewsJson(dto));
        } catch (NotFoundException e) {
            responseEntity = getNotFoundResponseEntity(e.getMessage());
        }
        return responseEntity;
    }

    @RequestMapping(value = "/news/{newsId}", method = RequestMethod.GET)
    public ResponseEntity findGeneralNewsById(@PathVariable long newsId) {
        ResponseEntity responseEntity;
        try {
            NewsDTO dto = newsService.findGeneralNewsById(newsId);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(new NewsJson(dto));
        } catch (NotFoundException e) {
            responseEntity = getNotFoundResponseEntity(e.getMessage());
        } catch (LogicException e) {
            responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Selecting all news by status")
    @RequestMapping(value = "/admins/news", method = RequestMethod.GET)
    public ResponseEntity getCollection(@RequestParam(name = "filter",
            required = false, defaultValue = "false") boolean filter, @RequestParam(name = "client",
            required = false, defaultValue = "false") boolean client) {
        Collection<NewsDTO> dtos;
        if (filter) {
            if (client) {
                dtos = newsService.getAllNewsByStatus(NewsStatus.CLIENT);
            } else {
                dtos = newsService.getAllNewsByStatus(NewsStatus.GENERAL);
            }
        } else {
            dtos = newsService.getAllNews();
        }
        return ResponseEntity.status(HttpStatus.OK).body(convertToArray(dtos));
    }

    @ApiOperation(value = "Sending news to clients")
    @RequestMapping(value = "/admins/news/{newsId}", method = RequestMethod.POST)
    public ResponseEntity sendNewsToClients(@PathVariable long newsId, @RequestBody Collection<Long> clientIds) {
        ResponseEntity responseEntity;
        try {
            NewsDTO dto = newsService.addClientNews(clientIds, newsId);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(new NewsJson(dto));
        } catch (NotFoundException e) {
            responseEntity = getNotFoundResponseEntity(e.getMessage());
        } catch (LogicException e) {
            responseEntity = getInternalServerErrorResponseEntity(e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Remarking news")
    @RequestMapping(value = "/admins/news/{newsId}", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody NewsDTO requestNewsDTO, @PathVariable long newsId) {
        ResponseEntity responseEntity;
        try {
            requestNewsDTO.setId(newsId);
            NewsDTO dto = newsService.update(requestNewsDTO);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(new NewsJson(dto));
        } catch (NotFoundException e) {
            responseEntity = getNotFoundResponseEntity(e.getMessage());
        }
        return responseEntity;
    }

    private ResponseEntity getNotFoundResponseEntity(String exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }

    private ResponseEntity getInternalServerErrorResponseEntity(String exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception);
    }

    private NewsJson[] convertToArray(Collection<NewsDTO> dtos) {
        return dtos.stream().map(NewsJson::new).toArray(NewsJson[]::new);
    }
}
