package gts.spring.conferences.mapper;

import gts.spring.conferences.dto.AlbumCollectionDTO;
import gts.spring.conferences.entity.AlbumCollection;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { TrackMapper.class, ArtistMapper.class })
public interface AlbumCollectionMapper {

    @Mapping(target = "tracks", source = "tracks")
    @Mapping(target = "artists", source = "artists")
    AlbumCollectionDTO toDTO(AlbumCollection entity);

    @Mapping(target = "tracks", ignore = true)
    @Mapping(target = "artists", ignore = true)
    AlbumCollection toEntity(AlbumCollectionDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tracks", ignore = true)
    @Mapping(target = "artists", ignore = true)
    void updateEntityFromDTO(AlbumCollectionDTO dto, @MappingTarget AlbumCollection entity);
}
