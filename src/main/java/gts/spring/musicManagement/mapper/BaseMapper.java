package gts.spring.musicManagement.mapper;

import gts.spring.musicManagement.dto.BaseEntityDTO;
import gts.spring.musicManagement.entity.BaseEntity;
import org.mapstruct.MappingTarget;

public interface BaseMapper<T extends BaseEntity, U extends BaseEntityDTO> {

    U toDTO(T entity);
    T toEntity(U dto);
    void updateEntityFromDTO(U dto, @MappingTarget T entity);
}