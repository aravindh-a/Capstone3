package gts.spring.conferences.mapper;

import gts.spring.conferences.dto.BaseEntityDTO;
import gts.spring.conferences.entity.BaseEntity;
import org.mapstruct.MappingTarget;

public interface BaseMapper<T extends BaseEntity, U extends BaseEntityDTO> {

    U toDTO(T entity);
    T toEntity(U dto);
    void updateEntityFromDTO(U dto, @MappingTarget T entity);
}