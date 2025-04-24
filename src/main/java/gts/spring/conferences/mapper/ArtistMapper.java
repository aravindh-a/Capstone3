package gts.spring.conferences.mapper;

import gts.spring.conferences.dto.ArtistDTO;
import gts.spring.conferences.entity.Artist;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    ArtistDTO toDTO(Artist entity);

    @Mapping(target = "albumCollections", ignore = true)
    Artist toEntity(ArtistDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "albumCollections", ignore = true)
    void updateEntityFromDTO(ArtistDTO dto, @MappingTarget Artist entity);
}
