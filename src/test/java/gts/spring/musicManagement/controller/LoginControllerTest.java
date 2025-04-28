package gts.spring.musicManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gts.spring.musicManagement.dto.LoginRequestDTO;
import gts.spring.musicManagement.entity.User;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;

    @BeforeEach
    void setup() throws Exception {
        // Simulate a login with a known USER account
        LoginRequestDTO loginRequest = new LoginRequestDTO("testuser", "password");

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the token
        userToken = objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void userShouldNotAccessAdminOnlyEndpoint() throws Exception {
        // Attempt to call an ADMIN-protected endpoint
        mockMvc.perform(post("/api/tracks") // Assuming /api/track requires ADMIN
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Course\",  \"duration\": 1.5}"))
                .andExpect(status().isForbidden()); // <--- Expect 403 FORBIDDEN
    }
}


