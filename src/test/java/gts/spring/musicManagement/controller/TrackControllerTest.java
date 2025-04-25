package gts.spring.musicManagement.controller;

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
class TrackControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataSource dataSource;

    private String baseUrl;

    @BeforeEach
    void setup() throws Exception {
        baseUrl = "http://localhost:" + port + "/api/tracks";

        // Clean up presenter/session join table if needed
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Clear join table first to avoid FK violations
            stmt.executeUpdate("DELETE FROM album_collection_track");
            stmt.executeUpdate("DELETE FROM track");
        }
    }

    @Test
    void createAndGetPresenter() {
        TrackDTO dto = new TrackDTO();
        dto.setTitle("Test Title");
        dto.setDuration(4.2);

        ResponseEntity<TrackDTO> createResponse = restTemplate.postForEntity(baseUrl, dto, TrackDTO.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TrackDTO created = createResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Test Title");

        ResponseEntity<TrackDTO> getResponse = restTemplate.getForEntity(baseUrl + "/" + created.getId(), TrackDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getTitle()).isEqualTo("Test Title");
        assertThat(Objects.requireNonNull(getResponse.getBody()).getDuration()).isEqualTo(4.2);
    }

    @Test
    void getAllPresenters_ShouldReturnList() {
        TrackDTO dto = new TrackDTO();
        dto.setTitle("Test Title2");
        dto.setDuration(4.2);
        restTemplate.postForEntity(baseUrl, dto, TrackDTO.class);

        ResponseEntity<List<TrackDTO>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void updatePresenter_ShouldModifyExisting() {
        TrackDTO dto = new TrackDTO();
        dto.setTitle("Title 3");
        dto.setDuration(4.2);
        TrackDTO created = restTemplate.postForEntity(baseUrl, dto, TrackDTO.class).getBody();

        assert created != null;
        created.setTitle("3rd Title");
        created.setDuration(8.5);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TrackDTO> entity = new HttpEntity<>(created, headers);

        ResponseEntity<TrackDTO> updateResponse = restTemplate.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.PUT,
                entity,
                TrackDTO.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(updateResponse.getBody()).getTitle()).isEqualTo("3rd Title");
        assertThat(Objects.requireNonNull(updateResponse.getBody()).getDuration()).isEqualTo(8.5);
    }

    @Test
    void deletePresenter_ShouldReturnNoContent() {
        TrackDTO dto = new TrackDTO();
        dto.setTitle("Moonlight Sonata");
        dto.setDuration(4.2);

        TrackDTO created = restTemplate.postForEntity(baseUrl, dto, TrackDTO.class).getBody();

        assert created != null;
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<TrackDTO> getAfterDelete = restTemplate.getForEntity(baseUrl + "/" + created.getId(), TrackDTO.class);
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
