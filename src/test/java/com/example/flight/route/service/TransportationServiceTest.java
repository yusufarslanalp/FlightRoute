package com.example.flight.route.service;

import com.example.flight.route.dto.CreateTransportationDto;
import com.example.flight.route.dto.TransportationDto;
import com.example.flight.route.dto.UpdateTransportationDto;
import com.example.flight.route.exception.InvalidRequest;
import com.example.flight.route.model.Day;
import com.example.flight.route.model.Location;
import com.example.flight.route.model.Transportation;
import com.example.flight.route.model.TransportationType;
import com.example.flight.route.repository.TransportationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransportationServiceTest {

    @Mock
    private TransportationRepository transportationRepository;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private TransportationService transportationService;

    @Test
    void getTransportations_mapsEntitiesToDtos() {
        Location from = Location.builder().id(1L).name("A").build();
        Location to = Location.builder().id(2L).name("B").build();
        Transportation t = Transportation.builder()
                .id(10L)
                .type(TransportationType.FLIGHT)
                .from(from)
                .to(to)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        when(transportationRepository.findAll()).thenReturn(List.of(t));

        List<TransportationDto> result = transportationService.getTransportations();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(10L);
        assertThat(result.get(0).getType()).isEqualTo("FLIGHT");
        assertThat(result.get(0).getFrom()).isEqualTo("A");
        assertThat(result.get(0).getTo()).isEqualTo("B");
        assertThat(result.get(0).getDays()).containsExactly("MONDAY");
    }

    @Test
    void saveTransportation_sameOriginAndDestination_throwsInvalidRequest() {
        CreateTransportationDto dto = CreateTransportationDto.builder()
                .fromId(1L)
                .toId(1L)
                .type(TransportationType.BUS)
                .days(List.of("MONDAY"))
                .build();

        assertThatThrownBy(() -> transportationService.saveTransportation(dto))
                .isInstanceOf(InvalidRequest.class)
                .hasMessageContaining("origin and destination");
    }

    @Test
    void saveTransportation_persistsAndReturnsDto() {
        Location from = Location.builder().id(1L).name("X").build();
        Location to = Location.builder().id(2L).name("Y").build();
        when(locationService.findById(1L)).thenReturn(from);
        when(locationService.findById(2L)).thenReturn(to);

        Transportation saved = Transportation.builder()
                .id(7L)
                .type(TransportationType.SUBWAY)
                .from(from)
                .to(to)
                .operatingDays(Day.toByte(List.of(Day.TUESDAY)))
                .build();
        when(transportationRepository.save(any(Transportation.class))).thenReturn(saved);

        CreateTransportationDto dto = CreateTransportationDto.builder()
                .fromId(1L)
                .toId(2L)
                .type(TransportationType.SUBWAY)
                .days(List.of("TUESDAY"))
                .build();

        TransportationDto result = transportationService.saveTransportation(dto);

        assertThat(result.getId()).isEqualTo(7L);
        assertThat(result.getType()).isEqualTo("SUBWAY");
        assertThat(result.getDays()).containsExactly("TUESDAY");

        ArgumentCaptor<Transportation> captor = ArgumentCaptor.forClass(Transportation.class);
        verify(transportationRepository).save(captor.capture());
        assertThat(captor.getValue().getFrom()).isSameAs(from);
        assertThat(captor.getValue().getTo()).isSameAs(to);
        assertThat(captor.getValue().getType()).isEqualTo(TransportationType.SUBWAY);
    }

    @Test
    void updateTransportation_updatesFieldsAndReturnsDto() {
        Location from = Location.builder().id(1L).name("A").build();
        Location to = Location.builder().id(2L).name("B").build();
        Transportation existing = Transportation.builder()
                .id(3L)
                .type(TransportationType.BUS)
                .from(from)
                .to(to)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        when(transportationRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(transportationRepository.save(existing)).thenReturn(existing);

        UpdateTransportationDto dto = UpdateTransportationDto.builder()
                .type(TransportationType.UBER)
                .days(List.of("FRIDAY", "SATURDAY"))
                .build();

        TransportationDto result = transportationService.updateTransportation(3L, dto);

        assertThat(result.getType()).isEqualTo("UBER");
        assertThat(result.getDays()).containsExactlyInAnyOrder("FRIDAY", "SATURDAY");
        verify(transportationRepository).save(existing);
    }

    @Test
    void deleteTransportation_delegatesToRepository() {
        transportationService.deleteTransportation(99L);
        verify(transportationRepository).deleteById(99L);
    }
}
