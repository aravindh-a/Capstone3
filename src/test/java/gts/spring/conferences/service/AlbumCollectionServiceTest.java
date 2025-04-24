package gts.spring.conferences.service;

import gts.spring.conferences.dto.AlbumCollectionDTO;
import gts.spring.conferences.entity.AlbumCollection;
import gts.spring.conferences.entity.Artist;
import gts.spring.conferences.entity.Track;
import gts.spring.conferences.mapper.AlbumCollectionMapper;
import gts.spring.conferences.repository.ArtistRepository;
import gts.spring.conferences.repository.AlbumCollectionRepository;
import gts.spring.conferences.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AlbumCollectionServiceTest {

    @Mock private AlbumCollectionRepository sessionRepository;
    @Mock private ArtistRepository artistRepository;
    @Mock private TrackRepository trackRepository;
    @Mock private AlbumCollectionMapper sessionMapper;

    @InjectMocks
    private AlbumCollectionService sessionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnDTOList() {
        AlbumCollection session = new AlbumCollection();
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        when(sessionRepository.findAllByOrderByIdAsc()).thenReturn(List.of(session));
        when(sessionMapper.toDTO(session)).thenReturn(dto);

        List<AlbumCollectionDTO> result = sessionService.findAll();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void findById_ShouldReturnDTO_WhenFound() {
        AlbumCollection session = new AlbumCollection();
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionMapper.toDTO(session)).thenReturn(dto);

        AlbumCollectionDTO result = sessionService.findById(1L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void findById_ShouldReturnNull_WhenNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        AlbumCollectionDTO result = sessionService.findById(1L);

        assertThat(result).isNull();
    }

    @Test
    void create_ShouldSaveAndReturnDTO() {
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        AlbumCollection session = new AlbumCollection();
        when(sessionMapper.toEntity(dto)).thenReturn(session);
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDTO(session)).thenReturn(dto);

        AlbumCollectionDTO result = sessionService.create(dto);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void update_ShouldModifyAndReturnDTO_WhenFound() {
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        AlbumCollection session = new AlbumCollection();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDTO(session)).thenReturn(dto);

        AlbumCollectionDTO result = sessionService.update(1L, dto);

        verify(sessionMapper).updateEntityFromDTO(dto, session);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void update_ShouldReturnNull_WhenNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        AlbumCollectionDTO result = sessionService.update(1L, new AlbumCollectionDTO());

        assertThat(result).isNull();
    }

    @Test
    void delete_ShouldCallRepository() {
        sessionService.delete(1L);

        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void registerAttendee_ShouldAddAttendee_WhenBothExist() {
        AlbumCollection session = new AlbumCollection();
        session.setArtists(new HashSet<>());
        Artist artist = new Artist();
        AlbumCollectionDTO dto = new AlbumCollectionDTO();

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(artistRepository.findById(2L)).thenReturn(Optional.of(artist));
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDTO(session)).thenReturn(dto);

        AlbumCollectionDTO result = sessionService.registerAttendee(1L, 2L);

        assertThat(session.getArtists()).contains(artist);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void registerAttendee_ShouldReturnNull_WhenSessionOrAttendeeMissing() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThat(sessionService.registerAttendee(1L, 2L)).isNull();

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(new AlbumCollection()));
        when(artistRepository.findById(2L)).thenReturn(Optional.empty());
        assertThat(sessionService.registerAttendee(1L, 2L)).isNull();
    }

    @Test
    void assignPresenter_ShouldAddPresenter_WhenBothExist() {
        AlbumCollection session = new AlbumCollection();
        session.setTracks(new HashSet<>());
        Track track = new Track();
        AlbumCollectionDTO dto = new AlbumCollectionDTO();

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(trackRepository.findById(2L)).thenReturn(Optional.of(track));
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDTO(session)).thenReturn(dto);

        AlbumCollectionDTO result = sessionService.assignPresenter(1L, 2L);

        assertThat(session.getTracks()).contains(track);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void assignPresenter_ShouldReturnNull_WhenSessionOrPresenterMissing() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThat(sessionService.assignPresenter(1L, 2L)).isNull();

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(new AlbumCollection()));
        when(trackRepository.findById(2L)).thenReturn(Optional.empty());
        assertThat(sessionService.assignPresenter(1L, 2L)).isNull();
    }

    @Test
    void findByAttendeeId_ShouldFilterSessionsWithAttendee() {
        Artist artist = new Artist(); artist.setId(1L);
        AlbumCollection session = new AlbumCollection();
        session.setArtists(Set.of(artist));
        AlbumCollectionDTO dto = new AlbumCollectionDTO();

        when(sessionRepository.findAllByOrderByIdAsc()).thenReturn(List.of(session));
        when(sessionMapper.toDTO(session)).thenReturn(dto);

        List<AlbumCollectionDTO> result = sessionService.findByAttendeeId(1L);

        assertThat(result).containsExactly(dto);
    }

    @Test
    void findByPresenterId_ShouldFilterSessionsWithPresenter() {
        Track track = new Track(); track.setId(1L);
        AlbumCollection session = new AlbumCollection();
        session.setTracks(Set.of(track));
        AlbumCollectionDTO dto = new AlbumCollectionDTO();

        when(sessionRepository.findAllByOrderByIdAsc()).thenReturn(List.of(session));
        when(sessionMapper.toDTO(session)).thenReturn(dto);

        List<AlbumCollectionDTO> result = sessionService.findByPresenterId(1L);

        assertThat(result).containsExactly(dto);
    }
}
