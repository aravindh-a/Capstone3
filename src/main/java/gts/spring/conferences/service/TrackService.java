package gts.spring.conferences.service;

import gts.spring.conferences.dto.TrackDTO;
import gts.spring.conferences.entity.AlbumCollection;
import gts.spring.conferences.entity.Track;
import gts.spring.conferences.mapper.TrackMapper;
import gts.spring.conferences.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    public List<TrackDTO> findAll() {
        return trackRepository.findAllByOrderByIdAsc()
                .stream()
                .map(trackMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TrackDTO findById(Long id) {
        return trackRepository.findById(id)
                .map(trackMapper::toDTO)
                .orElse(null);
    }

    @Transactional
    public TrackDTO create(TrackDTO trackDTO) {
        Track track = trackMapper.toEntity(trackDTO);
        return trackMapper.toDTO(trackRepository.save(track));
    }

    @Transactional
    public TrackDTO update(Long id, TrackDTO trackDTO) {
        Track existing = trackRepository.findById(id)
                .orElse(null);
        if (existing == null) {
            return null;
        }
        trackMapper.updateEntityFromDTO(trackDTO, existing);
        return trackMapper.toDTO(trackRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        Track track = trackRepository.findById(id).orElse(null);
        if (track == null) return;

        for (AlbumCollection session: track.getAlbumCollections()) {
            session.getTracks().remove(track);
        }

        track.getAlbumCollections().clear();
        trackRepository.deleteById(id);
    }
}
