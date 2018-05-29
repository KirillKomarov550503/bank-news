package com.netcracker.komarov.news.validator.impl;

import com.netcracker.komarov.news.service.dto.entity.NewsDTO;
import com.netcracker.komarov.news.service.exception.ValidationException;
import com.netcracker.komarov.news.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NewsValidator implements Validator<NewsDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsValidator.class);

    @Override
    public void validate(NewsDTO newsDTO) throws ValidationException {
        if (newsDTO.getTitle().isEmpty()) {
            String error = "Title can not be empty";
            LOGGER.error(error);
            throw new ValidationException(error);
        }
        if (newsDTO.getTitle().length() > 255) {
            String error = "Title is to much long";
            LOGGER.error(error);
            throw new ValidationException(error);
        }
        if (newsDTO.getText().isEmpty()) {
            String error = "Text can not be empty";
            LOGGER.error(error);
            throw new ValidationException(error);
        }
        if (newsDTO.getStatus() == null) {
            String error = "Unknown value for status";
            LOGGER.error(error);
            throw new ValidationException(error);
        }
        switch (newsDTO.getStatus()) {
            case "CLIENT":
                break;
            case "GENERAL":
                break;
            default:
                String error = "Unknown value for status";
                LOGGER.error(error);
                throw new ValidationException(error);

        }
    }
}
