package com.netcracker.komarov.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcracker.komarov.news.dao.entity.NewsStatus;
import com.netcracker.komarov.news.service.NewsService;
import com.netcracker.komarov.news.service.dto.entity.NewsDTO;
import com.netcracker.komarov.news.service.exception.LogicException;
import com.netcracker.komarov.news.service.exception.NotFoundException;
import com.netcracker.komarov.news.service.exception.ValidationException;
import com.netcracker.komarov.news.validator.impl.NewsValidator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/bank/v1")
public class NewsController {
    private NewsService newsService;
    private NewsValidator newsValidator;
    private ObjectMapper objectMapper;

    @Autowired
    public NewsController(NewsService newsService, NewsValidator newsValidator, ObjectMapper objectMapper) {
        this.newsService = newsService;
        this.newsValidator = newsValidator;
        this.objectMapper = objectMapper;
    }

    @ApiOperation(value = "Creation of new news")
    @RequestMapping(value = "/admins/{adminId}/news", method = RequestMethod.POST)
    public ResponseEntity save(@PathVariable long adminId, @RequestBody NewsDTO newsDTO) {
        ResponseEntity responseEntity;
        try {
            newsValidator.validate(newsDTO);
            NewsDTO dto = newsService.save(newsDTO, adminId);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (ValidationException e) {
            responseEntity = getBadRequestResponseEntity(e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Selecting all client news by client ID")
    @RequestMapping(value = "/clients/{clientId}/news", method = RequestMethod.GET)
    public ResponseEntity findAllNewsByClientId(@PathVariable long clientId) {
        ResponseEntity responseEntity;
        try {
            Collection<NewsDTO> dtos = newsService.findAllNewsByClientId(clientId);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(convertToArray(dtos));
        } catch (NotFoundException e) {
            responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Selecting all general news")
    @RequestMapping(value = "/news", method = RequestMethod.GET)
    public ResponseEntity findAllGeneralNews() {
        Collection<NewsDTO> dtos = newsService.findAllNewsByStatus(NewsStatus.GENERAL);
        return ResponseEntity.status(HttpStatus.OK).body(convertToArray(dtos));
    }

    @ApiOperation(value = "Select news by ID")
    @RequestMapping(value = "/news/{newsId}", method = RequestMethod.GET)
    public ResponseEntity findGeneralNewsById(@PathVariable long newsId) {
        ResponseEntity responseEntity;
        try {
            NewsDTO dto = newsService.findGeneralNewsById(newsId);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (NotFoundException e) {
            responseEntity = getNotFoundResponseEntity(e.getMessage());
        } catch (LogicException e) {
            responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Selecting news by ID")
    @RequestMapping(value = "/admins/news/{newsId}", method = RequestMethod.GET)
    public ResponseEntity findById(@PathVariable long newsId) {
        ResponseEntity responseEntity;
        try {
            NewsDTO dto = newsService.findById(newsId);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (NotFoundException e) {
            responseEntity = getNotFoundResponseEntity(e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Selecting all news by status")
    @RequestMapping(value = "/admins/news", method = RequestMethod.GET)
    public ResponseEntity findNewsByParams(@RequestParam Map<String, String> params) {
        ResponseEntity responseEntity;
        try {
            newsValidator.validateStatus(params.get("status"));
            Collection<NewsDTO> dtos = newsService.findAllNewsBySpecification(params);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(convertToArray(dtos));
        } catch (ValidationException e) {
            responseEntity = getBadRequestResponseEntity(e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Sending news to clients")
    @RequestMapping(value = "/admins/news/{newsId}", method = RequestMethod.POST)
    public ResponseEntity sendNewsToClients(@PathVariable long newsId, @RequestBody Collection<Long> clientIds) {
        ResponseEntity responseEntity;
        try {
            NewsDTO dto = newsService.sendNewsToClient(clientIds, newsId);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(dto);
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
            newsValidator.validate(requestNewsDTO);
            requestNewsDTO.setId(newsId);
            NewsDTO dto = newsService.update(requestNewsDTO);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (NotFoundException e) {
            responseEntity = getNotFoundResponseEntity(e.getMessage());
        } catch (ValidationException e) {
            responseEntity = getBadRequestResponseEntity(e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Delete news by ID")
    @RequestMapping(value = "/admins/news/{newsId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteById(@PathVariable long newsId) {
        ResponseEntity responseEntity;
        try {
            newsService.deleteById(newsId);
            responseEntity = ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotFoundException e) {
            responseEntity = getNotFoundResponseEntity(e.getMessage());
        }
        return responseEntity;
    }

    private ResponseEntity getNotFoundResponseEntity(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(objectMapper.valueToTree(message));
    }

    private ResponseEntity getInternalServerErrorResponseEntity(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(objectMapper.valueToTree(message));
    }

    private ResponseEntity getBadRequestResponseEntity(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(objectMapper.valueToTree(message));
    }

    private NewsDTO[] convertToArray(Collection<NewsDTO> dtos) {
        return dtos.toArray(new NewsDTO[0]);
    }
}
