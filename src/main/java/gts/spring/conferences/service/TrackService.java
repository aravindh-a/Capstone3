package gts.spring.conferences.service;

import gts.spring.conferences.dto.TrackDTO;
import gts.spring.conferences.entity.AlbumCollection;
import gts.spring.conferences.entity.Artist;
import gts.spring.conferences.entity.Track;
import gts.spring.conferences.mapper.TrackMapper;
import gts.spring.conferences.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class TrackService extends CrudService<Track,TrackDTO,TrackRepository, TrackMapper> {
    public TrackService(TrackRepository repository, TrackMapper mapper) {super(repository, mapper);}

    @Override
    @Transactional
    public void delete(Long id) {
        Track track = getRepository().findById(id).orElse(null);
        if (track == null) return;

        for (AlbumCollection albumCollection : track.getAlbumCollections()) {
            albumCollection.getTracks().remove(track);
        }

        track.getAlbumCollections().clear();
        super.delete(id);
    }
}
