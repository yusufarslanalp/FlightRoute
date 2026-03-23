package com.example.flight.route.service;

import com.example.flight.route.model.Location;
import com.example.flight.route.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    void getLocations_delegatesToRepository() {
        Location loc = Location.builder().id(1L).name("IST").build();
        when(locationRepository.findAll()).thenReturn(List.of(loc));

        assertThat(locationService.getLocations()).containsExactly(loc);
    }

    @Test
    void findById_returnsLocationFromRepository() {
        Location loc = Location.builder().id(5L).name("AMS").build();
        when(locationRepository.findById(5L)).thenReturn(Optional.of(loc));

        assertThat(locationService.findById(5L)).isSameAs(loc);
    }

    @Test
    void saveLocation_delegatesToRepository() {
        Location input = Location.builder().name("New").build();
        Location saved = Location.builder().id(9L).name("New").build();
        when(locationRepository.save(input)).thenReturn(saved);

        assertThat(locationService.saveLocation(input)).isSameAs(saved);
    }

    @Test
    void deleteLocation_delegatesToRepository() {
        locationService.deleteLocation(3L);
        verify(locationRepository).deleteById(3L);
    }
}
