package gts.spring.musicmanagement.mapper;

import gts.spring.musicmanagement.dto.ArtistDTO;
import gts.spring.musicmanagement.entity.Artist;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtistMapper extends BaseMapper<Artist, ArtistDTO>{
    @Override
    ArtistDTO toDTO(Artist entity);

    @Override
    @Mapping(target = "track",ignore = true)
    Artist toEntity(ArtistDTO dto);

    @Override
    @BeanMapping (nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tracks", ignore = true)
    void updateEntityFromDTO(ArtistDTO dto, @MappingTarget Artist entity);
}
