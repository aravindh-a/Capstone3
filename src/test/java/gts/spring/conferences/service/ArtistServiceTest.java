package gts.spring.conferences.service;

import gts.spring.conferences.dto.ArtistDTO;
import gts.spring.conferences.entity.Artist;
import gts.spring.conferences.mapper.ArtistMapper;
import gts.spring.conferences.repository.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ArtistMapper artistMapper;

    @InjectMocks
    private ArtistService artistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnListOfArtistDTOs() {
        Artist artist = new Artist();
        ArtistDTO artistDTO = new ArtistDTO();
        List<Artist> artists = List.of(artist);
        List<ArtistDTO> expected = List.of(artistDTO);

        when(artistRepository.findAllByOrderByIdAsc()).thenReturn(artists);
        when(artistMapper.toDTO(artist)).thenReturn(artistDTO);

        List<ArtistDTO> result = artistService.findAll();

        assertThat(result).isEqualTo(expected);
        verify(artistRepository).findAllByOrderByIdAsc();
    }

    @Test
    void findById_ShouldReturnArtistDTO_WhenFound() {
        Long id = 1L;
        Artist artist = new Artist();
        ArtistDTO artistDTO = new ArtistDTO();

        when(artistRepository.findById(id)).thenReturn(Optional.of(artist));
        when(artistMapper.toDTO(artist)).thenReturn(artistDTO);

        ArtistDTO result = artistService.findById(id);

        assertThat(result).isEqualTo(artistDTO);
    }

    @Test
    void findById_ShouldReturnNull_WhenNotFound() {
        Long id = 1L;

        when(artistRepository.findById(id)).thenReturn(Optional.empty());

        ArtistDTO result = artistService.findById(id);

        assertThat(result).isNull();
    }

    @Test
    void create_ShouldSaveAndReturnArtistDTO() {
        ArtistDTO dto = new ArtistDTO();
        Artist entity = new Artist();

        when(artistMapper.toEntity(dto)).thenReturn(entity);
        when(artistRepository.save(entity)).thenReturn(entity);
        when(artistMapper.toDTO(entity)).thenReturn(dto);

        ArtistDTO result = artistService.create(dto);

        assertThat(result).isEqualTo(dto);
        verify(artistRepository).save(entity);
    }

    @Test
    void update_ShouldModifyAndReturnUpdatedArtistDTO_WhenFound() {
        Long id = 1L;
        ArtistDTO dto = new ArtistDTO();
        Artist entity = new Artist();

        when(artistRepository.findById(id)).thenReturn(Optional.of(entity));
        when(artistRepository.save(entity)).thenReturn(entity);
        when(artistMapper.toDTO(entity)).thenReturn(dto);

        ArtistDTO result = artistService.update(id, dto);

        verify(artistMapper).updateEntityFromDTO(dto, entity);
        assertThat(result).isEqualTo(dto);
    }

    @Test
    void update_ShouldReturnNull_WhenArtistNotFound() {
        Long id = 1L;
        ArtistDTO dto = new ArtistDTO();

        when(artistRepository.findById(id)).thenReturn(Optional.empty());

        ArtistDTO result = artistService.update(id, dto);

        assertThat(result).isNull();
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        Long id = 1L;
        Artist entity = new Artist();

        when(artistRepository.findById(id)).thenReturn(Optional.of(entity));

        artistService.delete(id);

        verify(artistRepository).findById(id);
        verify(artistRepository).deleteById(id);
    }
}
