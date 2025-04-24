package gts.spring.conferences.controller;

import gts.spring.conferences.dto.AlbumCollectionDTO;
import gts.spring.conferences.dto.ArtistDTO;
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
import java.time.LocalDateTime;
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
        baseUrl = "http://localhost:" + port + "/api/sessions";
        try (Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM conference_session_attendee");
            stmt.executeUpdate("DELETE FROM conference_session_presenter");
            stmt.executeUpdate("DELETE FROM conference_session");
            stmt.executeUpdate("DELETE FROM attendee");
            stmt.executeUpdate("DELETE FROM presenter");
        }
    }

    @Test
    void createAndGetSession() {
        AlbumCollectionDTO dto = new AlbumCollectionDTO();
        dto.setTitle("Spring Boot Deep Dive");
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusHours(6));

        ResponseEntity<AlbumCollectionDTO> createResponse = restTemplate.postForEntity(baseUrl, dto, AlbumCollectionDTO.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        AlbumCollectionDTO created = createResponse.getBody();
        assertThat(created).isNotNull();

        ResponseEntity<AlbumCollectionDTO> getResponse = restTemplate.getForEntity(baseUrl + "/" + created.getId(), AlbumCollectionDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getTitle()).isEqualTo("Spring Boot Deep Dive");
    }

    @Test
    void assignPresenterAndRegisterAttendee() {
        // Create session
        AlbumCollectionDTO session = new AlbumCollectionDTO();
        session.setTitle("Test Session");
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(LocalDateTime.now().plusDays(4));
        session = restTemplate.postForEntity(baseUrl, session, AlbumCollectionDTO.class).getBody();

        // Create attendee
        ArtistDTO attendee = new ArtistDTO();
        attendee.setName("Test Attendee");
        attendee = restTemplate.postForEntity("http://localhost:" + port + "/api/attendees", attendee, ArtistDTO.class).getBody();

        // Create presenter
        TrackDTO presenter = new TrackDTO();
        presenter.setName("Test Presenter");
        presenter = restTemplate.postForEntity("http://localhost:" + port + "/api/presenters", presenter, TrackDTO.class).getBody();

        // Register attendee
        assert session != null;
        assert attendee != null;
        ResponseEntity<AlbumCollectionDTO> registerResponse = restTemplate.postForEntity(
                baseUrl + "/" + session.getId() + "/attendees/" + attendee.getId(),
                null, AlbumCollectionDTO.class);

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(registerResponse.getBody()).getAttendees()).hasSize(1);

        // Assign presenter
        assert presenter != null;
        ResponseEntity<AlbumCollectionDTO> assignResponse = restTemplate.postForEntity(
                baseUrl + "/" + session.getId() + "/presenters/" + presenter.getId(),
                null, AlbumCollectionDTO.class);

        assertThat(assignResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(assignResponse.getBody()).getPresenters()).hasSize(1);
    }

    @Test
    void getSessionsByAttendeeOrPresenter() {
        // Create session
        AlbumCollectionDTO session = new AlbumCollectionDTO();
        session.setTitle("Query Test");
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(LocalDateTime.now().plusHours(8));
        session = restTemplate.postForEntity(baseUrl, session, AlbumCollectionDTO.class).getBody();

        // Create and register attendee
        ArtistDTO attendee = new ArtistDTO();
        attendee.setName("Query Attendee");
        attendee = restTemplate.postForEntity("http://localhost:" + port + "/api/attendees", attendee, ArtistDTO.class).getBody();
        assert session != null;
        assert attendee != null;
        restTemplate.postForEntity(baseUrl + "/" + session.getId() + "/attendees/" + attendee.getId(), null, AlbumCollectionDTO.class);

        // Create and assign presenter
        TrackDTO presenter = new TrackDTO();
        presenter.setName("Query Presenter");
        presenter = restTemplate.postForEntity("http://localhost:" + port + "/api/presenters", presenter, TrackDTO.class).getBody();
        assert presenter != null;
        restTemplate.postForEntity(baseUrl + "/" + session.getId() + "/presenters/" + presenter.getId(), null, AlbumCollectionDTO.class);

        // Get by attendee
        ResponseEntity<List<AlbumCollectionDTO>> attendeeResponse = restTemplate.exchange(
                baseUrl + "/attendee/" + attendee.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(attendeeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(attendeeResponse.getBody()).hasSize(1);

        // Get by presenter
        ResponseEntity<List<AlbumCollectionDTO>> presenterResponse = restTemplate.exchange(
                baseUrl + "/presenter/" + presenter.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});

        assertThat(presenterResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(presenterResponse.getBody()).hasSize(1);
    }
}
