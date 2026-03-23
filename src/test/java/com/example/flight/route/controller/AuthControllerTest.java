package com.example.flight.route.controller;

import com.example.flight.route.model.User;
import com.example.flight.route.security.JwtUtil;
import com.example.flight.route.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .build();
        user = new User();
        user.setPasswordHash("hashedPassword");
    }

    @Nested
    @DisplayName("POST /auth/token")
    class GenerateToken {

        @Test
        @DisplayName("returns 401 when user is not found")
        void generateToken_userNotFound_returns401() throws Exception {
            when(userService.findByUsername("unknownUser")).thenReturn(Optional.empty());

            mockMvc.perform(post("/auth/token")
                            .param("username", "unknownUser")
                            .param("password", "anyPassword"))
                    .andExpect(status().isUnauthorized());

            verify(userService).findByUsername("unknownUser");
            verifyNoInteractions(passwordEncoder);
            verifyNoInteractions(jwtUtil);
        }

        @Test
        @DisplayName("returns 401 when password does not match")
        void generateToken_wrongPassword_returns401() throws Exception {
            when(userService.findByUsername("validUser")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

            mockMvc.perform(post("/auth/token")
                            .param("username", "validUser")
                            .param("password", "wrongPassword"))
                    .andExpect(status().isUnauthorized());

            verify(userService).findByUsername("validUser");
            verify(passwordEncoder).matches("wrongPassword", "hashedPassword");
            verifyNoInteractions(jwtUtil);
        }

        @Test
        @DisplayName("returns 400 when username is missing")
        void generateToken_missingUsername_returns400() throws Exception {
            mockMvc.perform(post("/auth/token")
                            .param("password", "anyPassword"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
            verifyNoInteractions(passwordEncoder);
            verifyNoInteractions(jwtUtil);
        }

        @Test
        @DisplayName("returns 400 when password is missing")
        void generateToken_missingPassword_returns400() throws Exception {
            mockMvc.perform(post("/auth/token")
                            .param("username", "validUser"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
            verifyNoInteractions(passwordEncoder);
            verifyNoInteractions(jwtUtil);
        }

        @Test
        @DisplayName("returns 400 when username is blank")
        void generateToken_blankUsername_returns400() throws Exception {
            mockMvc.perform(post("/auth/token")
                            .param("username", "   ")
                            .param("password", "anyPassword"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
            verifyNoInteractions(passwordEncoder);
            verifyNoInteractions(jwtUtil);
        }

        @Test
        @DisplayName("returns 400 when password is blank")
        void generateToken_blankPassword_returns400() throws Exception {
            mockMvc.perform(post("/auth/token")
                            .param("username", "validUser")
                            .param("password", "   "))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
            verifyNoInteractions(passwordEncoder);
            verifyNoInteractions(jwtUtil);
        }
    }
}