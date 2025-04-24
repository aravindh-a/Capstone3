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
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    public List<ArtistDTO> findAll() {
        return artistRepository.findAllByOrderByIdAsc()
                .stream()
                .map(artistMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ArtistDTO findById(Long id) {
        return artistRepository.findById(id)
                .map(artistMapper::toDTO)
                .orElse(null);
    }

    @Transactional
    public ArtistDTO create(ArtistDTO artistDTO) {
        Artist artist = artistMapper.toEntity(artistDTO);
        return artistMapper.toDTO(artistRepository.save(artist));
    }

    @Transactional
    public ArtistDTO update(Long id, ArtistDTO artistDTO) {
        Artist existing = artistRepository.findById(id)
                .orElse(null);
        if (existing == null) {
            return null;
        }
        artistMapper.updateEntityFromDTO(artistDTO, existing);
        return artistMapper.toDTO(artistRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        Artist artist = artistRepository.findById(id).orElse(null);
        if (artist == null) return;

        for (AlbumCollection session : artist.getAlbumCollections()) {
            session.getArtists().remove(artist);
        }

        artist.getAlbumCollections().clear();
        artistRepository.deleteById(id);
    }
}
