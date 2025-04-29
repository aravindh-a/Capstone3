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
class LoginControllerTest {

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

//    @Test
//    void login() throws Exception {
//        mockMvc.perform(post("/api/auth/register")
////                        .header("Authorization", "Bearer " + userToken)
//        .contentType(MediaType.APPLICATION_JSON)
//        .content("{\"username\":\"admin123\",\"password\":\"password123\",\"roles\":1}"))
//                .andExpect(status().isCreated());
//    }

    @Test
    void userShouldNotAccessAdminOnlyEndpoint() throws Exception {

        mockMvc.perform(post("/api/tracks")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Course\",  \"duration\": 1.5}"))
                .andExpect(status().isForbidden()); // <--- Expect 403 FORBIDDEN
    }
}


