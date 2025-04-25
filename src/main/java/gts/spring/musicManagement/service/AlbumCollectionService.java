package gts.spring.musicManagement.service;

import gts.spring.musicManagement.dto.AlbumCollectionDTO;
import gts.spring.musicManagement.entity.AlbumCollection;
import gts.spring.musicManagement.entity.Artist;
import gts.spring.musicManagement.entity.Track;
import gts.spring.musicManagement.mapper.AlbumCollectionMapper;
import gts.spring.musicManagement.repository.ArtistRepository;
import gts.spring.musicManagement.repository.AlbumCollectionRepository;
import gts.spring.musicManagement.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumCollectionService  {

    private final AlbumCollectionRepository albumCollectionRepository;
    private final ArtistRepository artistRepository;
    private final TrackRepository trackRepository;
    private final AlbumCollectionMapper albumCollectionMapper;

    public List<AlbumCollectionDTO> findAll() {
        return albumCollectionRepository.findAllByOrderByIdAsc()
                .stream()
                .map(albumCollectionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AlbumCollectionDTO findById(Long id) {
        return albumCollectionRepository.findById(id)
                .map(albumCollectionMapper::toDTO)
                .orElse(null);
    }

    @Transactional
    public AlbumCollectionDTO create(AlbumCollectionDTO sessionDTO) {
        AlbumCollection session = albumCollectionMapper.toEntity(sessionDTO);
        return albumCollectionMapper.toDTO(albumCollectionRepository.save(session));
    }

    @Transactional
    public AlbumCollectionDTO update(Long id, AlbumCollectionDTO sessionDTO) {
        AlbumCollection existing = albumCollectionRepository.findById(id)
                .orElse(null);
        if (existing == null) {
            return null;
        }
        albumCollectionMapper.updateEntityFromDTO(sessionDTO, existing);
        return albumCollectionMapper.toDTO(albumCollectionRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        albumCollectionRepository.deleteById(id);
    }

    @Transactional
    public AlbumCollectionDTO registerArtist(Long sessionId, Long attendeeId) {
        AlbumCollection session = albumCollectionRepository.findById(sessionId)
                .orElse(null);
        Artist artist = artistRepository.findById(attendeeId)
                .orElse(null);
        if (session == null || artist == null) {
            return null;
        }
        session.getArtists().add(artist);
        return albumCollectionMapper.toDTO(albumCollectionRepository.save(session));
    }

    @Transactional
    public AlbumCollectionDTO registerTrack(Long sessionId, Long trackId) {
        AlbumCollection session = albumCollectionRepository.findById(sessionId)
                .orElse(null);
        Track track = trackRepository.findById(trackId)
                .orElse(null);
        if (session == null || track == null) {
            return null;
        }
        session.getTracks().add(track);
        return albumCollectionMapper.toDTO(albumCollectionRepository.save(session));
    }

    public List<AlbumCollectionDTO> findByArtistId(Long artistId) {
        return albumCollectionRepository.findAllByOrderByIdAsc().stream()
                .filter(session -> session.getArtists().stream()
                        .anyMatch(attendee -> attendee.getId().equals(artistId)))
                .map(albumCollectionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<AlbumCollectionDTO> findByTrackId(Long trackId) {
        return albumCollectionRepository.findAllByOrderByIdAsc().stream()
                .filter(session -> session.getTracks().stream()
                        .anyMatch(presenter -> presenter.getId().equals(trackId)))
                .map(albumCollectionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
