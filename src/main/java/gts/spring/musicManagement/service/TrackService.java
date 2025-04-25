package gts.spring.musicManagement.service;

import gts.spring.musicManagement.dto.TrackDTO;
import gts.spring.musicManagement.entity.AlbumCollection;
import gts.spring.musicManagement.entity.Track;
import gts.spring.musicManagement.mapper.TrackMapper;
import gts.spring.musicManagement.repository.TrackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class TrackService extends CrudService<Track,TrackDTO,TrackRepository, TrackMapper> {
    public TrackService(TrackRepository repository, TrackMapper mapper) {super(repository, mapper);}

    @Override
    @Transactional
    public void delete(Long id) {
        Track track = getRepository().findById(id).orElse(null);
//        if (track == null) return;

        if (track != null) {
            for (AlbumCollection albumCollection : track.getAlbumCollections()) {
                albumCollection.getTracks().remove(track);
            }
            track.getAlbumCollections().clear();
            super.delete(id);
        }



    }
}
