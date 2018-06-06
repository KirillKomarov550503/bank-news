package com.netcracker.komarov.news.util.validator;

import java.io.Serializable;

public interface Validator<DTO extends Serializable> {
    void validate(DTO dto);
}
