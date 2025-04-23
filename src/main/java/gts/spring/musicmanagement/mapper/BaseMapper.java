package gts.spring.musicmanagement.mapper;

import gts.spring.musicmanagement.dto.BaseDTO;
import gts.spring.musicmanagement.entity.BaseEntity;
import org.mapstruct.MappingTarget;

public interface BaseMapper<T extends BaseEntity, U extends BaseDTO> {
    U toDTO(T entity);
    T toEntity(U dto);

    void updateEntityFromDTO(U dto, @MappingTarget T entity);
}
