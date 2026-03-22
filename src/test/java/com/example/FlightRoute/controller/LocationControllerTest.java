package com.example.FlightRoute.controller;

import com.example.FlightRoute.model.Location;
import com.example.FlightRoute.service.LocationService;
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
class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Location location;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
        objectMapper = new ObjectMapper();
        location = new Location();
    }

    @Nested
    @DisplayName("GET /location")
    class GetAllLocations {

        @Test
        @DisplayName("returns 200 and list of locations")
        void getAllLocations_returns200() throws Exception {
            when(locationService.getLocations()).thenReturn(List.of(location));

            mockMvc.perform(get("/location"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(1));

            verify(locationService).getLocations();
        }

        @Test
        @DisplayName("returns 200 and empty list when no locations exist")
        void getAllLocations_emptyList_returns200() throws Exception {
            when(locationService.getLocations()).thenReturn(List.of());

            mockMvc.perform(get("/location"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(locationService).getLocations();
        }
    }

    @Nested
    @DisplayName("POST /location")
    class CreateLocation {

        @Test
        @DisplayName("returns 400 when body is missing")
        void createLocation_missingBody_returns400() throws Exception {
            mockMvc.perform(post("/location")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(locationService);
        }
    }

    @Nested
    @DisplayName("PUT /location")
    class UpdateLocation {

        @Test
        @DisplayName("returns 400 when body is missing")
        void updateLocation_missingBody_returns400() throws Exception {
            mockMvc.perform(put("/location")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(locationService);
        }
    }

    @Nested
    @DisplayName("DELETE /location/{locationId}")
    class DeleteLocation {

        @Test
        @DisplayName("returns 200 when called with valid id")
        void deleteLocation_validId_returns200() throws Exception {
            doNothing().when(locationService).deleteLocation(1L);

            mockMvc.perform(delete("/location/1"))
                    .andExpect(status().isOk());

            verify(locationService).deleteLocation(1L);
        }

        @Test
        @DisplayName("returns 400 when id is not a valid number")
        void deleteLocation_invalidIdType_returns400() throws Exception {
            mockMvc.perform(delete("/location/abc"))
                    .andExpect(status().isBadRequest());

            verifyNoInteractions(locationService);
        }
    }
}