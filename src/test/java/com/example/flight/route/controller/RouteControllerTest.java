package com.example.flight.route.controller;

import com.example.flight.route.dto.RouteDto;
import com.example.flight.route.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RouteControllerTest {

    @Mock
    private RouteService routeService;

    @InjectMocks
    private RouteController routeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(routeController).build();
    }

    @Nested
    @DisplayName("GET /route")
    class GetAllRoutes {

        @Test
        @DisplayName("returns 200 and list of routes when all params are valid")
        void getRoutes_validParams_returns200() throws Exception {
            RouteDto routeDto = new RouteDto();
            when(routeService.getRoutes(1L, 2L, LocalDate.of(2025, 1, 1)))
                    .thenReturn(List.of(routeDto));

            mockMvc.perform(get("/route")
                            .param("fromId", "1")
                            .param("toId", "2")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(1));

            verify(routeService).getRoutes(1L, 2L, LocalDate.of(2025, 1, 1));
        }

        @Test
        @DisplayName("returns 200 and empty list when no routes found")
        void getRoutes_noResults_returns200() throws Exception {
            when(routeService.getRoutes(1L, 2L, LocalDate.of(2025, 1, 1)))
                    .thenReturn(List.of());

            mockMvc.perform(get("/route")
                            .param("fromId", "1")
                            .param("toId", "2")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(routeService).getRoutes(1L, 2L, LocalDate.of(2025, 1, 1));
        }

        @Test
        @DisplayName("returns 400 when fromId is missing")
        void getRoutes_missingFromId_returns400() throws Exception {
            mockMvc.perform(get("/route")
                            .param("toId", "2")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(routeService);
        }

        @Test
        @DisplayName("returns 400 when toId is missing")
        void getRoutes_missingToId_returns400() throws Exception {
            mockMvc.perform(get("/route")
                            .param("fromId", "1")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(routeService);
        }

        @Test
        @DisplayName("returns 400 when date is missing")
        void getRoutes_missingDate_returns400() throws Exception {
            mockMvc.perform(get("/route")
                            .param("fromId", "1")
                            .param("toId", "2"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(routeService);
        }

        @Test
        @DisplayName("returns 400 when date format is invalid")
        void getRoutes_invalidDateFormat_returns400() throws Exception {
            mockMvc.perform(get("/route")
                            .param("fromId", "1")
                            .param("toId", "2")
                            .param("date", "01-01-2025"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(routeService);
        }

        @Test
        @DisplayName("returns 400 when fromId is not a valid number")
        void getRoutes_invalidFromId_returns400() throws Exception {
            mockMvc.perform(get("/route")
                            .param("fromId", "abc")
                            .param("toId", "2")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(routeService);
        }

        @Test
        @DisplayName("returns 400 when toId is not a valid number")
        void getRoutes_invalidToId_returns400() throws Exception {
            mockMvc.perform(get("/route")
                            .param("fromId", "1")
                            .param("toId", "abc")
                            .param("date", "2025-01-01"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(routeService);
        }
    }
}