package gts.spring.conferences.service;

import gts.spring.conferences.dto.ArtistDTO;
import gts.spring.conferences.entity.AlbumCollection;
import gts.spring.conferences.entity.Artist;
import gts.spring.conferences.mapper.ArtistMapper;
import gts.spring.conferences.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ArtistService extends CrudService<Artist, ArtistDTO,ArtistRepository, ArtistMapper> {
    public ArtistService(ArtistRepository repository, ArtistMapper mapper) {super(repository, mapper);}

    @Override
    @Transactional
    public void delete(Long id) {
        Artist artist = getRepository().findById(id).orElse(null);
        if (artist == null) return;

        for (AlbumCollection albumCollection : artist.getAlbumCollections()) {
            albumCollection.getArtists().remove(artist);
        }

        artist.getAlbumCollections().clear();
        super.delete(id);
    }

}
