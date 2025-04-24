package gts.spring.conferences.controller;

import gts.spring.conferences.dto.TrackDTO;
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
        baseUrl = "http://localhost:" + port + "/api/presenters";

        // Clean up presenter/session join table if needed
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Clear join table first to avoid FK violations
            stmt.executeUpdate("DELETE FROM conference_session_presenter");
            stmt.executeUpdate("DELETE FROM presenter");
        }
    }

    @Test
    void createAndGetPresenter() {
        TrackDTO dto = new TrackDTO();
        dto.setName("Ada Lovelace");

        ResponseEntity<TrackDTO> createResponse = restTemplate.postForEntity(baseUrl, dto, TrackDTO.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TrackDTO created = createResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("Ada Lovelace");

        ResponseEntity<TrackDTO> getResponse = restTemplate.getForEntity(baseUrl + "/" + created.getId(), TrackDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getName()).isEqualTo("Ada Lovelace");
    }

    @Test
    void getAllPresenters_ShouldReturnList() {
        TrackDTO dto = new TrackDTO();
        dto.setName("Grace Hopper");
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
        dto.setName("Katherine Johnson");
        TrackDTO created = restTemplate.postForEntity(baseUrl, dto, TrackDTO.class).getBody();

        assert created != null;
        created.setName("Katherine J.");

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
        assertThat(Objects.requireNonNull(updateResponse.getBody()).getName()).isEqualTo("Katherine J.");
    }

    @Test
    void deletePresenter_ShouldReturnNoContent() {
        TrackDTO dto = new TrackDTO();
        dto.setName("Alan Turing");

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
