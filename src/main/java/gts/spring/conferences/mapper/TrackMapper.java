package gts.spring.conferences.mapper;

import gts.spring.conferences.dto.TrackDTO;
import gts.spring.conferences.entity.Track;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TrackMapper extends BaseMapper<Track, TrackDTO> {

    @Override
    TrackDTO toDTO(Track entity);

    @Override
    @Mapping(target = "albumCollections", ignore = true)
    Track toEntity(TrackDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "albumCollections", ignore = true)
    void updateEntityFromDTO(TrackDTO dto, @MappingTarget Track entity);
}
