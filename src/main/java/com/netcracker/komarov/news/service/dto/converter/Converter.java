package com.netcracker.komarov.news.service.dto.converter;

import java.io.Serializable;

public interface Converter<DTO extends Serializable, Entity> {
    DTO convertToDTO(Entity entity);

    Entity convertToEntity(DTO dto);
}
