package com.netcracker.komarov.news.service.dto.converter;

public interface Converter<DTO, Entity> {
    DTO convertToDTO(Entity entity);

    Entity convertToEntity(DTO dto);
}
