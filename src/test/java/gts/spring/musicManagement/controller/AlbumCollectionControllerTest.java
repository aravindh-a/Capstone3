package gts.spring.musicManagement.controller;

import gts.spring.musicManagement.dto.AlbumCollectionDTO;
import gts.spring.musicManagement.dto.ArtistDTO;
import gts.spring.musicManagement.dto.TrackDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AlbumCollectionControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataSource dataSource;

    private String baseUrl;

    @BeforeEach
    void setup() throws Exception {
        baseUrl = "http://localhost:" + port + "/api/albums";
        try (Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM album_collection_artist");
            stmt.executeUpdate("DELETE FROM album_collection_track");
            stmt.executeUpdate("DELETE FROM album_collection");
            stmt.executeUpdate("DELETE FROM artist");
            stmt.executeUpdate("DELETE FROM track");
        }
    }

    @Test
    void createAndGetAlbumCollection() {
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        dto.setAlbumName("Album1");
        dto.setGenre("Genre1");

        ResponseEntity<AlbumCollectionDTO> createResponse = restTemplate.postForEntity(baseUrl, dto, AlbumCollectionDTO.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        AlbumCollectionDTO created = createResponse.getBody();
        assertThat(created).isNotNull();

        ResponseEntity<AlbumCollectionDTO> getResponse = restTemplate.getForEntity(baseUrl + "/" + created.getId(), AlbumCollectionDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getAlbumName()).isEqualTo("Album1");
    }

    @Test
    void registerTrackAndRegisterArtist() {
        // Create Album
        AlbumCollectionDTO albumCollectionDTO = new AlbumCollectionDTO();
        albumCollectionDTO.setAlbumName("Test Album");
        albumCollectionDTO.setGenre("Test Genre1");
        albumCollectionDTO = restTemplate.postForEntity(baseUrl, albumCollectionDTO, AlbumCollectionDTO.class).getBody();

        // Create artist
        ArtistDTO artist = new ArtistDTO();
        artist.setArtistName("Test Artist");
        artist.setCountry("Test Country");
        artist = restTemplate.postForEntity("http://localhost:" + port + "/api/artists", artist, ArtistDTO.class).getBody();

        // Create track
        TrackDTO track = new TrackDTO();
        track.setTitle("Test track Name");
        track.setDuration(2.06);
        track = restTemplate.postForEntity("http://localhost:" + port + "/api/tracks", track, TrackDTO.class).getBody();

        // Register artist
        assert albumCollectionDTO != null;
        assert artist != null;
        ResponseEntity<AlbumCollectionDTO> registerResponse = restTemplate.postForEntity(
                baseUrl + "/" + albumCollectionDTO.getId() + "/artists/" + artist.getId(),
                null, AlbumCollectionDTO.class);

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(registerResponse.getBody()).getArtists()).hasSize(1);

        // Assign track
        assert track != null;
        ResponseEntity<AlbumCollectionDTO> assignResponse = restTemplate.postForEntity(
                baseUrl + "/" + albumCollectionDTO.getId() + "/tracks/" + track.getId(),
                null, AlbumCollectionDTO.class);

        assertThat(assignResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(assignResponse.getBody()).getTracks()).hasSize(1);
    }

    @Test
    void getAlbumsByArtistOrTrack() {
        // Create album
        AlbumCollectionDTO albumCollectionDTO = new AlbumCollectionDTO();
        albumCollectionDTO.setAlbumName("Test Album Name");
        albumCollectionDTO.setGenre("Pop");
        albumCollectionDTO = restTemplate.postForEntity(baseUrl, albumCollectionDTO, AlbumCollectionDTO.class).getBody();

        // Create and register Artist
        ArtistDTO artist = new ArtistDTO();
        artist.setArtistName("Test Artist Name");
        artist.setCountry("Test Country");
        artist = restTemplate.postForEntity("http://localhost:" + port + "/api/artists", artist, ArtistDTO.class).getBody();
        assert albumCollectionDTO != null;
        assert artist != null;
        restTemplate.postForEntity(baseUrl + "/" + albumCollectionDTO.getId() + "/artists/" + artist.getId(), null, AlbumCollectionDTO.class);

        // Create and assign Track
        TrackDTO track = new TrackDTO();
        track.setTitle("Test Track Name");
        track.setDuration(2.06);
        track = restTemplate.postForEntity("http://localhost:" + port + "/api/tracks", track, TrackDTO.class).getBody();
        assert track != null;
        restTemplate.postForEntity(baseUrl + "/" + albumCollectionDTO.getId() + "/tracks/" + track.getId(), null, AlbumCollectionDTO.class);

        // Get by Artist
        ResponseEntity<List<AlbumCollectionDTO>> artistResponse = restTemplate.exchange(
                baseUrl + "/artists/" + artist.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(artistResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(artistResponse.getBody()).hasSize(1);

        // Get by track
        ResponseEntity<List<AlbumCollectionDTO>> trackResponse = restTemplate.exchange(
                baseUrl + "/tracks/" + track.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(trackResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(trackResponse.getBody()).hasSize(1);
    }
}
