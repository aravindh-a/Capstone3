package gts.spring.musicManagement.service;

import gts.spring.musicManagement.dto.TrackDTO;
import gts.spring.musicManagement.entity.Track;
import gts.spring.musicManagement.mapper.TrackMapper;
import gts.spring.musicManagement.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TrackServiceTest {

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private TrackMapper trackMapper;

    @InjectMocks
    private TrackService trackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void findAll_ShouldReturnListOfTrackDTOs() {
//        Track track = new Track();
//        TrackDTO presenterDTO = new TrackDTO();
//        List<Track> presenters = List.of(track);
//        List<TrackDTO> expected = List.of(presenterDTO);
//
//        when(trackRepository.findAllByOrderByIdAsc()).thenReturn(presenters);
//        when(trackMapper.toDTO(track)).thenReturn(presenterDTO);
//
//        List<TrackDTO> result = trackService.findAll();
//
//        assertThat(result).isEqualTo(expected);
//        verify(trackRepository).findAllByOrderByIdAsc();
//    }

    @Test
    void findAll_ShouldReturnListOfTrackDTOs() {
        Track track = new Track();
        TrackDTO trackDTO = new TrackDTO();
        List<TrackDTO> trackDTOs = new ArrayList<>();
        List<Track> tracks = new ArrayList<>();

        when(trackRepository.findAll()).thenReturn(tracks);
        when(trackMapper.toDTO(track)).thenReturn(trackDTO);

        List<TrackDTO> trackDTOsFound = trackService.findAll();
        assertThat(trackDTOsFound).isEqualTo(trackDTOs);
        verify(trackRepository).findAll(Sort.by(Sort.Direction.ASC, "id"));

    }
    @Test
    void findById_ShouldReturnTrackDTO_WhenFound() {
        Long id = 1L;
        Track track = new Track();
        TrackDTO trackDTO = new TrackDTO();

        when(trackRepository.findById(id)).thenReturn(Optional.of(track));
        when(trackMapper.toDTO(track)).thenReturn(trackDTO);

        TrackDTO result = trackService.findById(id);

        assertThat(result).isEqualTo(trackDTO);
    }

    @Test
    void findById_ShouldReturnNull_WhenNotFound() {
        Long id = 1L;

        when(trackRepository.findById(id)).thenReturn(Optional.empty());

        TrackDTO result = trackService.findById(id);

        assertThat(result).isNull();
    }

    @Test
    void create_ShouldSaveAndReturnTrackDTO() {
        TrackDTO dto = new TrackDTO();
        Track entity = new Track();

        when(trackMapper.toEntity(dto)).thenReturn(entity);
        when(trackRepository.save(entity)).thenReturn(entity);
        when(trackMapper.toDTO(entity)).thenReturn(dto);

        TrackDTO result = trackService.create(dto);

        assertThat(result).isEqualTo(dto);
        verify(trackRepository).save(entity);
    }

    @Test
    void update_ShouldModifyAndReturnUpdatedTrackDTO_WhenFound() {
        Long id = 1L;
        TrackDTO dto = new TrackDTO();
        Track entity = new Track();

        when(trackRepository.findById(id)).thenReturn(Optional.of(entity));
        when(trackRepository.save(entity)).thenReturn(entity);
        when(trackMapper.toDTO(entity)).thenReturn(dto);

        TrackDTO result = trackService.update(id, dto);

        verify(trackMapper).updateEntityFromDTO(dto, entity);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void update_ShouldReturnNull_WhenTrackNotFound() {
        Long id = 1L;
        TrackDTO dto = new TrackDTO();

        when(trackRepository.findById(id)).thenReturn(Optional.empty());

        TrackDTO result = trackService.update(id, dto);

        assertThat(result).isNull();
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        Long id = 1L;
        Track track = new Track();

        when(trackRepository.findById(id)).thenReturn(Optional.of(track));

        trackService.delete(id);

        verify(trackRepository).findById(id);
        verify(trackRepository).deleteById(id);
    }

}
