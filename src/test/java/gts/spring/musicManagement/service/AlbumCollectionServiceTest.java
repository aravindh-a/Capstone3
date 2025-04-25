package gts.spring.musicManagement.service;

import gts.spring.musicManagement.dto.AlbumCollectionDTO;
import gts.spring.musicManagement.entity.AlbumCollection;
import gts.spring.musicManagement.entity.Artist;
import gts.spring.musicManagement.entity.Track;
import gts.spring.musicManagement.mapper.AlbumCollectionMapper;
import gts.spring.musicManagement.repository.ArtistRepository;
import gts.spring.musicManagement.repository.AlbumCollectionRepository;
import gts.spring.musicManagement.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AlbumCollectionServiceTest {

    @Mock private AlbumCollectionRepository albumCollectionRepository;
    @Mock private ArtistRepository artistRepository;
    @Mock private TrackRepository trackRepository;
    @Mock private AlbumCollectionMapper albumCollectionMapper;

    @InjectMocks
    private AlbumCollectionService albumCollectionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnDTOList() {
        AlbumCollection albumCollection = new AlbumCollection();
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        when(albumCollectionRepository.findAllByOrderByIdAsc()).thenReturn(List.of(albumCollection));
        when(albumCollectionMapper.toDTO(albumCollection)).thenReturn(dto);

        List<AlbumCollectionDTO> result = albumCollectionService.findAll();

        assertThat(result).containsExactly(dto);
    }

    @Test
    void findById_ShouldReturnDTO_WhenFound() {
        AlbumCollection albumCollection = new AlbumCollection();
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        when(albumCollectionRepository.findById(1L)).thenReturn(Optional.of(albumCollection));
        when(albumCollectionMapper.toDTO(albumCollection)).thenReturn(dto);

        AlbumCollectionDTO result = albumCollectionService.findById(1L);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void findById_ShouldReturnNull_WhenNotFound() {
        when(albumCollectionRepository.findById(1L)).thenReturn(Optional.empty());

        AlbumCollectionDTO result = albumCollectionService.findById(1L);

        assertThat(result).isNull();
    }

    @Test
    void create_ShouldSaveAndReturnDTO() {
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        AlbumCollection albumCollection = new AlbumCollection();
        when(albumCollectionMapper.toEntity(dto)).thenReturn(albumCollection);
        when(albumCollectionRepository.save(albumCollection)).thenReturn(albumCollection);
        when(albumCollectionMapper.toDTO(albumCollection)).thenReturn(dto);

        AlbumCollectionDTO result = albumCollectionService.create(dto);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void update_ShouldModifyAndReturnDTO_WhenFound() {
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        AlbumCollection albumCollection = new AlbumCollection();
        when(albumCollectionRepository.findById(1L)).thenReturn(Optional.of(albumCollection));
        when(albumCollectionRepository.save(albumCollection)).thenReturn(albumCollection);
        when(albumCollectionMapper.toDTO(albumCollection)).thenReturn(dto);

        AlbumCollectionDTO result = albumCollectionService.update(1L, dto);

        verify(albumCollectionMapper).updateEntityFromDTO(dto, albumCollection);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void update_ShouldReturnNull_WhenNotFound() {
        when(albumCollectionRepository.findById(1L)).thenReturn(Optional.empty());

        AlbumCollectionDTO result = albumCollectionService.update(1L, new AlbumCollectionDTO());

        assertThat(result).isNull();
    }

    @Test
    void delete_ShouldCallRepository() {
        albumCollectionService.delete(1L);

        verify(albumCollectionRepository).deleteById(1L);
    }

    @Test
    void registerArtist_ShouldAddArtist_WhenBothExist() {
        AlbumCollection albumCollection = new AlbumCollection();
        albumCollection.setArtists(new HashSet<>());
        Artist artist = new Artist();
        AlbumCollectionDTO dto = new AlbumCollectionDTO();

        when(albumCollectionRepository.findById(1L)).thenReturn(Optional.of(albumCollection));
        when(artistRepository.findById(2L)).thenReturn(Optional.of(artist));
        when(albumCollectionRepository.save(albumCollection)).thenReturn(albumCollection);
        when(albumCollectionMapper.toDTO(albumCollection)).thenReturn(dto);

        AlbumCollectionDTO result = albumCollectionService.registerArtist(1L, 2L);

        assertThat(albumCollection.getArtists()).contains(artist);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void registerArtist_ShouldReturnNull_WhenalbumCollectionorArtistMissing() {
        when(albumCollectionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThat(albumCollectionService.registerArtist(1L, 2L)).isNull();

        when(albumCollectionRepository.findById(1L)).thenReturn(Optional.of(new AlbumCollection()));
        when(artistRepository.findById(2L)).thenReturn(Optional.empty());
        assertThat(albumCollectionService.registerArtist(1L, 2L)).isNull();
    }

    @Test
    void registerTrack_ShouldAddTrack_WhenBothExist() {
        AlbumCollection albumCollection = new AlbumCollection();
        albumCollection.setTracks(new HashSet<>());
        Track track = new Track();
        AlbumCollectionDTO dto = new AlbumCollectionDTO();

        when(albumCollectionRepository.findById(1L)).thenReturn(Optional.of(albumCollection));
        when(trackRepository.findById(2L)).thenReturn(Optional.of(track));
        when(albumCollectionRepository.save(albumCollection)).thenReturn(albumCollection);
        when(albumCollectionMapper.toDTO(albumCollection)).thenReturn(dto);

        AlbumCollectionDTO result = albumCollectionService.registerTrack(1L, 2L);

        assertThat(albumCollection.getTracks()).contains(track);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void registerTrack_ShouldReturnNull_WhenAlbumsOrTrackMissing() {
        when(albumCollectionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThat(albumCollectionService.registerTrack(1L, 2L)).isNull();

        when(albumCollectionRepository.findById(1L)).thenReturn(Optional.of(new AlbumCollection()));
        when(trackRepository.findById(2L)).thenReturn(Optional.empty());
        assertThat(albumCollectionService.registerTrack(1L, 2L)).isNull();
    }

    @Test
    void findByArtistId_ShouldFilterAlbumsWithArtist() {
        Artist artist = new Artist(); artist.setId(1L);
        AlbumCollection albumCollection = new AlbumCollection();
        albumCollection.setArtists(Set.of(artist));
        AlbumCollectionDTO dto = new AlbumCollectionDTO();

        when(albumCollectionRepository.findAllByOrderByIdAsc()).thenReturn(List.of(albumCollection));
        when(albumCollectionMapper.toDTO(albumCollection)).thenReturn(dto);

        List<AlbumCollectionDTO> result = albumCollectionService.findByArtistId(1L);

        assertThat(result).containsExactly(dto);
    }

    @Test
    void findByTrackId_ShouldFilterAlbumsWithTrack() {
        Track track = new Track(); track.setId(1L);
        AlbumCollection albumCollection = new AlbumCollection();
        albumCollection.setTracks(Set.of(track));
        AlbumCollectionDTO dto = new AlbumCollectionDTO();

        when(albumCollectionRepository.findAllByOrderByIdAsc()).thenReturn(List.of(albumCollection));
        when(albumCollectionMapper.toDTO(albumCollection)).thenReturn(dto);

        List<AlbumCollectionDTO> result = albumCollectionService.findByTrackId(1L);

        assertThat(result).containsExactly(dto);
    }
}
