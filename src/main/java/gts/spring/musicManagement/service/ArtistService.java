package gts.spring.musicManagement.service;

import gts.spring.musicManagement.dto.ArtistDTO;
import gts.spring.musicManagement.entity.AlbumCollection;
import gts.spring.musicManagement.entity.Artist;
import gts.spring.musicManagement.mapper.ArtistMapper;
import gts.spring.musicManagement.repository.ArtistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
