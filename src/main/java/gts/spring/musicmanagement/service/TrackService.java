package gts.spring.musicmanagement.service;

import gts.spring.musicmanagement.dto.TrackDTO;
import gts.spring.musicmanagement.entity.Track;
import gts.spring.musicmanagement.mapper.TrackMapper;
import gts.spring.musicmanagement.repository.TrackRepository;
import org.springframework.stereotype.Service;

@Service
public class TrackService extends CrudService<Track, TrackDTO, TrackRepository, TrackMapper> {
    public TrackService(TrackRepository repository, TrackMapper mapper) {super(repository, mapper);}
}
