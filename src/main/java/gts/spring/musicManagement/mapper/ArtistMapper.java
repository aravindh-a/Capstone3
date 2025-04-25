package gts.spring.musicManagement.mapper;

import gts.spring.musicManagement.dto.ArtistDTO;
import gts.spring.musicManagement.entity.Artist;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtistMapper extends BaseMapper<Artist, ArtistDTO> {
    @Override
    ArtistDTO toDTO(Artist entity);

    @Override
    @Mapping(target = "albumCollections", ignore = true)
    Artist toEntity(ArtistDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "albumCollections", ignore = true)
    void updateEntityFromDTO(ArtistDTO dto, @MappingTarget Artist entity);
}
