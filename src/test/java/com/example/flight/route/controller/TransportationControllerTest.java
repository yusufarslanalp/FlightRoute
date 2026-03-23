package com.example.flight.route.controller;

import com.example.flight.route.dto.CreateTransportationDto;
import com.example.flight.route.dto.TransportationDto;
import com.example.flight.route.dto.UpdateTransportationDto;
import com.example.flight.route.model.TransportationType;
import com.example.flight.route.service.TransportationService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransportationControllerTest {

    @Mock
    private TransportationService transportationService;

    @InjectMocks
    private TransportationController transportationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private TransportationDto transportationDto;
    private CreateTransportationDto createTransportationDto;
    private UpdateTransportationDto updateTransportationDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transportationController).build();
        objectMapper = new ObjectMapper();
        transportationDto = new TransportationDto(
                1L,
                "BUS",
                "Istanbul",
                "Ankara",
                List.of("MONDAY", "TUESDAY")
        );

        createTransportationDto = new CreateTransportationDto(
                1L,
                2L,
                TransportationType.BUS,
                List.of("MONDAY")
        );

        updateTransportationDto = new UpdateTransportationDto(
                TransportationType.BUS,
                List.of("MONDAY")
        );
    }

    @Nested
    @DisplayName("GET /transportation")
    class GetTransportations {

        @Test
        @DisplayName("returns 200 and list")
        void getTransportations_returns200() throws Exception {
            when(transportationService.getTransportations())
                    .thenReturn(List.of(transportationDto));

            mockMvc.perform(get("/transportation"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(1));

            verify(transportationService).getTransportations();
        }

        @Test
        @DisplayName("returns empty list when no transportations exist")
        void getTransportations_emptyList_returns200() throws Exception {
            when(transportationService.getTransportations()).thenReturn(List.of());

            mockMvc.perform(get("/transportation"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("POST /transportation")
    class CreateTransportation {

        @Test
        @DisplayName("returns 400 when body is missing")
        void createTransportation_missingBody_returns400() throws Exception {
            mockMvc.perform(post("/transportation")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(transportationService);
        }
    }

    @Nested
    @DisplayName("PUT /transportation/{transportationId}")
    class UpdateTransportation {

        @Test
        @DisplayName("returns 400 when body is missing")
        void updateTransportation_missingBody_returns400() throws Exception {
            mockMvc.perform(put("/transportation/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(transportationService);
        }
    }

    @Nested
    @DisplayName("DELETE /transportation/{transportationId}")
    class DeleteTransportation {

        @Test
        @DisplayName("returns 200 when called with valid id")
        void deleteTransportation_returns200() throws Exception {
            doNothing().when(transportationService).deleteTransportation(1L);

            mockMvc.perform(delete("/transportation/1"))
                    .andExpect(status().isOk());

            verify(transportationService).deleteTransportation(1L);
        }
    }
}