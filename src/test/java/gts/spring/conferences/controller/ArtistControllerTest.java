package gts.spring.conferences.controller;

import gts.spring.conferences.dto.ArtistDTO;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ArtistControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataSource dataSource;

    private String baseUrl;

    @BeforeEach
    void setup() throws SQLException {
        baseUrl = "http://localhost:" + port + "/api/artists";

        // Clean up attendee/session join table if needed
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Clear join table first to avoid FK violations
            stmt.executeUpdate("DELETE FROM album_collection_artist");
            stmt.executeUpdate("DELETE FROM artist");
        }
    }

    @Test
    void createAndGetArtist() {
        ArtistDTO dto = new ArtistDTO();
        dto.setArtistName("Alice Johnson");

        ResponseEntity<ArtistDTO> createResponse = restTemplate.postForEntity(baseUrl, dto, ArtistDTO.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ArtistDTO created = createResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getArtistName()).isEqualTo("Alice Johnson");

        ResponseEntity<ArtistDTO> getResponse = restTemplate.getForEntity(baseUrl + "/" + created.getId(), ArtistDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getArtistName()).isEqualTo("Alice Johnson");
    }

    @Test
    void getAllArtists_ShouldReturnList() {
        ArtistDTO dto1 = new ArtistDTO();
        dto1.setArtistName("John Doe");

        restTemplate.postForEntity(baseUrl, dto1, ArtistDTO.class);

        ResponseEntity<List<ArtistDTO>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void updateAttendee_ShouldModifyExisting() {
        ArtistDTO dto = new ArtistDTO();
        dto.setArtistName("Jane Smith");

        ArtistDTO created = restTemplate.postForEntity(baseUrl, dto, ArtistDTO.class).getBody();
        assert created != null;
        created.setArtistName("Jane Doe");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ArtistDTO> entity = new HttpEntity<>(created, headers);

        ResponseEntity<ArtistDTO> updateResponse = restTemplate.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.PUT,
                entity,
                ArtistDTO.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(updateResponse.getBody()).getArtistName()).isEqualTo("Jane Doe");
    }

    @Test
    void deleteAttendee_ShouldReturnNoContent() {
        ArtistDTO dto = new ArtistDTO();
        dto.setArtistName("Bob Brown");

        ArtistDTO created = restTemplate.postForEntity(baseUrl, dto, ArtistDTO.class).getBody();

        assert created != null;
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<ArtistDTO> getAfterDelete = restTemplate.getForEntity(baseUrl + "/" + created.getId(), ArtistDTO.class);
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
