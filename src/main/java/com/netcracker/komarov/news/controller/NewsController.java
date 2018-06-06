package com.netcracker.komarov.news.controller;

import com.netcracker.komarov.news.ErrorJson;
import com.netcracker.komarov.news.dao.entity.NewsStatus;
import com.netcracker.komarov.news.service.NewsService;
import com.netcracker.komarov.news.service.dto.entity.NewsDTO;
import com.netcracker.komarov.news.service.exception.LogicException;
import com.netcracker.komarov.news.service.exception.NotFoundException;
import com.netcracker.komarov.news.service.exception.ValidationException;
import com.netcracker.komarov.news.util.validator.impl.NewsValidator;
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
    private ErrorJson errorJson;

    @Autowired
    public NewsController(NewsService newsService, NewsValidator newsValidator, ErrorJson errorJson) {
        this.newsService = newsService;
        this.newsValidator = newsValidator;
        this.errorJson = errorJson;
    }

    @ApiOperation(value = "Create of new news")
    @RequestMapping(value = "/admins/{adminId}/news", method = RequestMethod.POST)
    public ResponseEntity save(@PathVariable long adminId, @RequestBody NewsDTO newsDTO) {
        ResponseEntity responseEntity;
        try {
            newsValidator.validate(newsDTO);
            NewsDTO dto = newsService.save(newsDTO, adminId);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (ValidationException e) {
            responseEntity = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Select all client news by client ID")
    @RequestMapping(value = "/clients/{clientId}/news", method = RequestMethod.GET)
    public ResponseEntity findAllNewsByClientId(@PathVariable long clientId) {
        ResponseEntity responseEntity;
        Collection<NewsDTO> dtos = newsService.findAllNewsByClientId(clientId);
        responseEntity = ResponseEntity.status(HttpStatus.OK).body(convertToArray(dtos));
        return responseEntity;
    }

    @ApiOperation(value = "Select all general news")
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
        } catch (LogicException e) {
            responseEntity = getErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Select news by ID")
    @RequestMapping(value = "/admins/news/{newsId}", method = RequestMethod.GET)
    public ResponseEntity findById(@PathVariable long newsId) {
        ResponseEntity responseEntity;
        try {
            NewsDTO dto = newsService.findById(newsId);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (NotFoundException e) {
            responseEntity = getErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Select all news by status")
    @RequestMapping(value = "/admins/news", method = RequestMethod.GET)
    public ResponseEntity findNewsByParams(@RequestParam Map<String, String> params) {
        ResponseEntity responseEntity;
        try {

            newsValidator.validateParams(params);
            Collection<NewsDTO> dtos = newsService.findAllNewsBySpecification(params);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(convertToArray(dtos));
        } catch (ValidationException e) {
            responseEntity = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Send news to clients")
    @RequestMapping(value = "/admins/news/{newsId}", method = RequestMethod.POST)
    public ResponseEntity sendNewsToClients(@PathVariable long newsId, @RequestBody Collection<Long> clientIds) {
        ResponseEntity responseEntity;
        try {
            NewsDTO dto = newsService.sendNewsToClient(clientIds, newsId);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (NotFoundException e) {
            responseEntity = getErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (LogicException e) {
            responseEntity = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return responseEntity;
    }

    @ApiOperation(value = "Remark news")
    @RequestMapping(value = "/admins/news/{newsId}", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody NewsDTO requestNewsDTO, @PathVariable long newsId) {
        ResponseEntity responseEntity;
        try {
            newsValidator.validate(requestNewsDTO);
            requestNewsDTO.setId(newsId);
            NewsDTO dto = newsService.update(requestNewsDTO);
            responseEntity = ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (NotFoundException e) {
            responseEntity = getErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ValidationException e) {
            responseEntity = getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
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
            responseEntity = getErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return responseEntity;
    }

    private ResponseEntity getErrorResponse(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(errorJson.getErrorJson(message));
    }

    private NewsDTO[] convertToArray(Collection<NewsDTO> dtos) {
        return dtos.toArray(new NewsDTO[0]);
    }
}
