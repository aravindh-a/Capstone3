package gts.spring.musicManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gts.spring.musicManagement.dto.ArtistDTO;
import gts.spring.musicManagement.dto.LoginRequestDTO;
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

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;
    private String baselogin;
    private String userToken;
    @BeforeEach
    void setup() throws Exception {
        baseUrl = "http://localhost:" + port + "/api/artists";
        baselogin = "http://localhost:"+ port + "/api/auth/login";

        // Clean up Artist/album join table if needed
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Clear join table first to avoid FK violations
            stmt.executeUpdate("DELETE FROM album_collection_artist");
            stmt.executeUpdate("DELETE FROM artist");

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
    void createAndGetArtist() {
        ArtistDTO dto = new ArtistDTO();
        dto.setArtistName("Alice Johnson");
        dto.setCountry("India");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<ArtistDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<ArtistDTO> createResponse = restTemplate.postForEntity(baseUrl, request, ArtistDTO.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ArtistDTO createdArtist = createResponse.getBody();
        assertThat(createdArtist).isNotNull();
        assertThat(createdArtist.getArtistName()).isEqualTo("Alice Johnson");
        assertThat(createdArtist.getCountry()).isEqualTo("India");

        ResponseEntity<ArtistDTO> getResponse = restTemplate.exchange(baseUrl + "/" + createdArtist.getId()
                ,HttpMethod.GET
                ,request
                ,ArtistDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getArtistName()).isEqualTo("Alice Johnson");

//        ResponseEntity<ArtistDTO> createResponse = restTemplate.postForEntity(baseUrl, dto, ArtistDTO.class);
//        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        ArtistDTO created = createResponse.getBody();
//        assertThat(created).isNotNull();
//        assertThat(created.getArtistName()).isEqualTo("Alice Johnson");
//
//        ResponseEntity<ArtistDTO> getResponse = restTemplate.getForEntity(baseUrl + "/" + created.getId(), ArtistDTO.class);
//        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(Objects.requireNonNull(getResponse.getBody()).getArtistName()).isEqualTo("Alice Johnson");
    }

    @Test
    void getAllArtists_ShouldReturnList() {
        ArtistDTO dto1 = new ArtistDTO();
        dto1.setArtistName("John Doe");
        dto1.setCountry("India");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<ArtistDTO> request = new HttpEntity<>(dto1, headers);

        restTemplate.postForEntity(baseUrl, request, ArtistDTO.class);

        ResponseEntity<List<ArtistDTO>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void updateArtist_ShouldModifyExisting() {
        ArtistDTO dto = new ArtistDTO();
        dto.setArtistName("Jane Smith");
        dto.setCountry("India");
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        headers1.setBearerAuth(userToken);
        HttpEntity<ArtistDTO> request = new HttpEntity<>(dto, headers1);

        ArtistDTO created = restTemplate.postForEntity(baseUrl, request, ArtistDTO.class).getBody();
        assert created != null;
        created.setArtistName("Jane Doe");
        created.setCountry("Russia");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<ArtistDTO> entity = new HttpEntity<>(created, headers);

        ResponseEntity<ArtistDTO> updateResponse = restTemplate.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.PUT,
                entity,
                ArtistDTO.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(updateResponse.getBody()).getArtistName()).isEqualTo("Jane Doe");
        assertThat(Objects.requireNonNull(updateResponse.getBody()).getCountry()).isEqualTo("Russia");
    }

    @Test
    void deleteArtist_ShouldReturnNoContent() {
        ArtistDTO dto = new ArtistDTO();
        dto.setArtistName("Bob Brown");
        dto.setCountry("India");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<ArtistDTO> request = new HttpEntity<>(dto, headers);


        ArtistDTO created = restTemplate.postForEntity(baseUrl, request, ArtistDTO.class).getBody();

        assert created != null;
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + created.getId(),
                HttpMethod.DELETE,
                request,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<ArtistDTO> getAfterDelete = restTemplate.exchange(baseUrl + "/" + created.getId(),HttpMethod.GET,request , ArtistDTO.class);
        assertThat(getAfterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
