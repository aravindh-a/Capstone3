package gts.spring.conferences.service;

import gts.spring.conferences.dto.TrackDTO;
import gts.spring.conferences.entity.Track;
import gts.spring.conferences.mapper.TrackMapper;
import gts.spring.conferences.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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

    @Test
    void findAll_ShouldReturnListOfPresenterDTOs() {
        Track track = new Track();
        TrackDTO trackDTO = new TrackDTO();
        List<Track> tracks = List.of(track);
        List<TrackDTO> expected = List.of(trackDTO);

        when(trackRepository.findAllByOrderByIdAsc()).thenReturn(tracks);
        when(trackMapper.toDTO(track)).thenReturn(trackDTO);

        List<TrackDTO> result = trackService.findAll();

        assertThat(result).isEqualTo(expected);
        verify(trackRepository).findAllByOrderByIdAsc();
    }

    @Test
    void findById_ShouldReturnPresenterDTO_WhenFound() {
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
    void create_ShouldSaveAndReturnPresenterDTO() {
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
    void update_ShouldModifyAndReturnUpdatedPresenterDTO_WhenFound() {
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
    void update_ShouldReturnNull_WhenPresenterNotFound() {
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
