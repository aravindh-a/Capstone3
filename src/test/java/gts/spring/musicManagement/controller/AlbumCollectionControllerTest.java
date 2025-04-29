package gts.spring.musicManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gts.spring.musicManagement.dto.AlbumCollectionDTO;
import gts.spring.musicManagement.dto.ArtistDTO;
import gts.spring.musicManagement.dto.LoginRequestDTO;
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
    private ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataSource dataSource;

    private String baseUrl;
    private String baselogin;
    private String userToken;

    @BeforeEach
    void setup() throws Exception {
        baseUrl = "http://localhost:" + port + "/api/albums";
        baselogin = "http://localhost:"+ port + "/api/auth/login";
        try (Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM album_collection_artist");
            stmt.executeUpdate("DELETE FROM album_collection_track");
            stmt.executeUpdate("DELETE FROM album_collection");
            stmt.executeUpdate("DELETE FROM artist");
            stmt.executeUpdate("DELETE FROM track");
        }
        userToken = obtainAccessToken("testadmin","adminpassword");
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequestDTO> request = new HttpEntity<>(loginRequestDTO, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baselogin, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        userToken = objectMapper.readTree(response.getBody()).get("token").asText();
        System.out.println(userToken);
        return userToken;

    }

    @Test
    void createAndGetAlbumCollection() {
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        dto.setAlbumName("Album1");
        dto.setGenre("Genre1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<AlbumCollectionDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<AlbumCollectionDTO> createResponse = restTemplate.postForEntity(baseUrl, request, AlbumCollectionDTO.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        AlbumCollectionDTO created = createResponse.getBody();
        assertThat(created).isNotNull();

        ResponseEntity<AlbumCollectionDTO> getResponse = restTemplate.exchange(baseUrl + "/" + created.getId(),HttpMethod.GET,request, AlbumCollectionDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getAlbumName()).isEqualTo("Album1");
    }

    @Test
    void getAllAlbum_ShouldReturnList() {
        AlbumCollectionDTO dto1 = new AlbumCollectionDTO();
        dto1.setAlbumName("Test Album");
        dto1.setGenre("POP");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<AlbumCollectionDTO> request = new HttpEntity<>(dto1, headers);

        restTemplate.postForEntity(baseUrl, request, AlbumCollectionDTO.class);

        ResponseEntity<List<AlbumCollectionDTO>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void registerTrackAndRegisterArtist() {
        // Create Album
        AlbumCollectionDTO albumCollectionDTO = new AlbumCollectionDTO();
        albumCollectionDTO.setAlbumName("Test Album");
        albumCollectionDTO.setGenre("Test Genre1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<AlbumCollectionDTO> request = new HttpEntity<>(albumCollectionDTO, headers);
        albumCollectionDTO = restTemplate.postForEntity(baseUrl, request, AlbumCollectionDTO.class).getBody();

        // Create artist
        ArtistDTO artist = new ArtistDTO();
        artist.setArtistName("Test Artist");
        artist.setCountry("Test Country");
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        headers1.setBearerAuth(userToken);
        HttpEntity<ArtistDTO> request1 = new HttpEntity<>(artist, headers1);
        artist = restTemplate.postForEntity("http://localhost:" + port + "/api/artists", request1, ArtistDTO.class).getBody();

        // Create track
        TrackDTO track = new TrackDTO();
        track.setTitle("Test track Name");
        track.setDuration(2.06);
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_JSON);
        headers2.setBearerAuth(userToken);
        HttpEntity<TrackDTO> request2 = new HttpEntity<>(track, headers2);
        track = restTemplate.postForEntity("http://localhost:" + port + "/api/tracks", request2, TrackDTO.class).getBody();

        // Register artist
        assert albumCollectionDTO != null;
        assert artist != null;
        ResponseEntity<AlbumCollectionDTO> registerResponse = restTemplate.postForEntity(
                baseUrl + "/" + albumCollectionDTO.getId() + "/artists/" + artist.getId(),
                request, AlbumCollectionDTO.class);

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(registerResponse.getBody()).getArtists()).hasSize(1);

        // Assign track
        assert track != null;
        ResponseEntity<AlbumCollectionDTO> assignResponse = restTemplate.postForEntity(
                baseUrl + "/" + albumCollectionDTO.getId() + "/tracks/" + track.getId(),
                request, AlbumCollectionDTO.class);

        assertThat(assignResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(assignResponse.getBody()).getTracks()).hasSize(1);
    }

    @Test
    void getAlbumsByArtistOrTrack() {
        // Create album
        AlbumCollectionDTO albumCollectionDTO = new AlbumCollectionDTO();
        albumCollectionDTO.setAlbumName("Test Album Name");
        albumCollectionDTO.setGenre("Pop");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<AlbumCollectionDTO> request = new HttpEntity<>(albumCollectionDTO, headers);
        albumCollectionDTO = restTemplate.postForEntity(baseUrl, request, AlbumCollectionDTO.class).getBody();

        // Create and register Artist
        ArtistDTO artist = new ArtistDTO();
        artist.setArtistName("Test Artist Name");
        artist.setCountry("Test Country");
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        headers1.setBearerAuth(userToken);
        HttpEntity<ArtistDTO> request1 = new HttpEntity<>(artist, headers1);
        artist = restTemplate.postForEntity("http://localhost:" + port + "/api/artists", request1, ArtistDTO.class).getBody();
        assert albumCollectionDTO != null;
        assert artist != null;
        restTemplate.postForEntity(baseUrl + "/" + albumCollectionDTO.getId() + "/artists/" + artist.getId(), request1, AlbumCollectionDTO.class);

        // Create and assign Track
        TrackDTO track = new TrackDTO();
        track.setTitle("Test Track Name");
        track.setDuration(2.06);
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_JSON);
        headers2.setBearerAuth(userToken);
        HttpEntity<TrackDTO> request2 = new HttpEntity<>(track, headers2);
        track = restTemplate.postForEntity("http://localhost:" + port + "/api/tracks", request2, TrackDTO.class).getBody();
        assert track != null;
        restTemplate.postForEntity(baseUrl + "/" + albumCollectionDTO.getId() + "/tracks/" + track.getId(), request2, AlbumCollectionDTO.class);

        // Get by Artist
        ResponseEntity<List<AlbumCollectionDTO>> artistResponse = restTemplate.exchange(
                baseUrl + "/artists/" + artist.getId(),
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {});

        assertThat(artistResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(artistResponse.getBody()).hasSize(1);

        // Get by track
        ResponseEntity<List<AlbumCollectionDTO>> trackResponse = restTemplate.exchange(
                baseUrl + "/tracks/" + track.getId(),
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {});

        assertThat(trackResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(trackResponse.getBody()).hasSize(1);
    }
    @Test
    void updateArtist_ShouldModifyExisting() {
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        dto.setAlbumName("Test Album Name");
        dto.setGenre("Rock");
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        headers1.setBearerAuth(userToken);
        HttpEntity<AlbumCollectionDTO> request = new HttpEntity<>(dto, headers1);

        AlbumCollectionDTO created = restTemplate.postForEntity(baseUrl, request, AlbumCollectionDTO.class).getBody();
        assert created != null;
        created.setAlbumName("Changed Album Name");
        created.setGenre("Classical");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<AlbumCollectionDTO> entity = new HttpEntity<>(created, headers);

        ResponseEntity<AlbumCollectionDTO> updateResponse = restTemplate.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.PUT,
                entity,
                AlbumCollectionDTO.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(updateResponse.getBody()).getAlbumName()).isEqualTo("Changed Album Name");
        assertThat(Objects.requireNonNull(updateResponse.getBody()).getGenre()).isEqualTo("Classical");
    }
    @Test
    void deleteAlbum_ShouldReturnNoContent() {
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        dto.setAlbumName("TestAlbum Name");
        dto.setGenre("Pop");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<AlbumCollectionDTO> request = new HttpEntity<>(dto, headers);


        AlbumCollectionDTO created = restTemplate.postForEntity(baseUrl, request, AlbumCollectionDTO.class).getBody();

        assert created != null;
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.DELETE,
                request,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<AlbumCollectionDTO> getAfterDelete = restTemplate.exchange(baseUrl + "/" + created.getId(),HttpMethod.GET,request , AlbumCollectionDTO.class);
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
