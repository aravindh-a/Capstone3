package gts.spring.musicmanagement.mapper;

import gts.spring.musicmanagement.dto.TrackDTO;
import gts.spring.musicmanagement.entity.Track;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TrackMapper extends BaseMapper<Track, TrackDTO> {

    @Override
    TrackDTO toDTO(Track entity);

    @Override
    Track toEntity(TrackDTO dto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "artists", ignore = true)
    void updateEntityFromDTO(TrackDTO dto, @MappingTarget Track entity);
}
