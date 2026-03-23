package com.example.flight.route.service;

import com.example.flight.route.dto.RouteDto;
import com.example.flight.route.exception.InvalidRequest;
import com.example.flight.route.model.Day;
import com.example.flight.route.model.Location;
import com.example.flight.route.model.Transportation;
import com.example.flight.route.model.TransportationType;
import com.example.flight.route.repository.LocationRepository;
import com.example.flight.route.repository.TransportationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.flight.route.model.TransportationType.FLIGHT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    private static final LocalDate MONDAY = LocalDate.of(2024, 1, 22);

    @Mock
    private TransportationRepository transportationRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private RouteService routeService;

    @Test
    void getRoutes_busFlightSubway_returnsThreeLegRoute() {
        Location locA = Location.builder().id(1L).name("A").build();
        Location locB = Location.builder().id(2L).name("B").build();
        Location locC = Location.builder().id(3L).name("C").build();
        Location locD = Location.builder().id(4L).name("D").build();

        Transportation bus = Transportation.builder()
                .id(10L)
                .type(TransportationType.BUS)
                .from(locA)
                .to(locB)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        Transportation flight = Transportation.builder()
                .id(11L)
                .type(FLIGHT)
                .from(locB)
                .to(locC)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        Transportation subway = Transportation.builder()
                .id(12L)
                .type(TransportationType.SUBWAY)
                .from(locC)
                .to(locD)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(locA));
        when(locationRepository.findById(4L)).thenReturn(Optional.of(locD));
        when(transportationRepository.findByFrom(locA)).thenReturn(List.of(bus));
        when(transportationRepository.findByTo(locD)).thenReturn(List.of(subway));
        when(transportationRepository.findByFromAndToAndType(eq(locA), eq(locD), eq(FLIGHT)))
                .thenReturn(List.of());
        when(transportationRepository.findByFromAndToAndType(eq(locB), eq(locC), eq(FLIGHT)))
                .thenReturn(List.of(flight));

        List<RouteDto> routes = routeService.getRoutes(1L, 4L, MONDAY);

        assertThat(routes).hasSize(1);
        assertThat(routes.get(0).getTransportations()).containsExactly(bus, flight, subway);
        assertThat(routes.get(0).getFlightIndex()).isEqualTo(1);
    }

    @Test
    void getRoutes_busSubwayFlight_returnsNoRoutes() {
        Location locA = Location.builder().id(1L).name("A").build();
        Location locB = Location.builder().id(2L).name("B").build();
        Location locC = Location.builder().id(3L).name("C").build();
        Location locD = Location.builder().id(4L).name("D").build();

        Transportation bus = Transportation.builder()
                .id(10L)
                .type(TransportationType.BUS)
                .from(locA)
                .to(locB)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        Transportation flight = Transportation.builder()
                .id(12L)
                .type(FLIGHT)
                .from(locC)
                .to(locD)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(locA));
        when(locationRepository.findById(4L)).thenReturn(Optional.of(locD));
        when(transportationRepository.findByFrom(locA)).thenReturn(List.of(bus));
        when(transportationRepository.findByTo(locD)).thenReturn(List.of(flight));
        when(transportationRepository.findByFromAndToAndType(eq(locA), eq(locD), eq(FLIGHT)))
                .thenReturn(List.of());

        List<RouteDto> routes = routeService.getRoutes(1L, 4L, MONDAY);

        assertThat(routes).isEmpty();
    }

    @Test
    void getRoutes_flightBusBus_returnsNoRoutes() {
        Location locA = Location.builder().id(1L).name("A").build();
        Location locB = Location.builder().id(2L).name("B").build();
        Location locC = Location.builder().id(3L).name("C").build();
        Location locD = Location.builder().id(4L).name("D").build();

        Transportation flight = Transportation.builder()
                .id(10L)
                .type(FLIGHT)
                .from(locA)
                .to(locB)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        Transportation bus2 = Transportation.builder()
                .id(12L)
                .type(TransportationType.BUS)
                .from(locC)
                .to(locD)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(locA));
        when(locationRepository.findById(4L)).thenReturn(Optional.of(locD));
        when(transportationRepository.findByFrom(locA)).thenReturn(List.of(flight));
        when(transportationRepository.findByTo(locD)).thenReturn(List.of(bus2));
        when(transportationRepository.findByFromAndToAndType(eq(locA), eq(locD), eq(FLIGHT)))
                .thenReturn(List.of());

        List<RouteDto> routes = routeService.getRoutes(1L, 4L, MONDAY);

        assertThat(routes).isEmpty();
    }

    @Test
    void getRoutes_busFlightSubwayUber_returnsNoRoutes() {
        Location locA = Location.builder().id(1L).name("A").build();
        Location locB = Location.builder().id(2L).name("B").build();
        Location locD = Location.builder().id(4L).name("D").build();
        Location locE = Location.builder().id(5L).name("E").build();

        Transportation bus = Transportation.builder()
                .id(10L)
                .type(TransportationType.BUS)
                .from(locA)
                .to(locB)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        Transportation uber = Transportation.builder()
                .id(13L)
                .type(TransportationType.UBER)
                .from(locD)
                .to(locE)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(locA));
        when(locationRepository.findById(5L)).thenReturn(Optional.of(locE));
        when(transportationRepository.findByFrom(locA)).thenReturn(List.of(bus));
        when(transportationRepository.findByTo(locE)).thenReturn(List.of(uber));
        when(transportationRepository.findByFromAndToAndType(eq(locA), eq(locE), eq(FLIGHT)))
                .thenReturn(List.of());
        when(transportationRepository.findByFromAndToAndType(eq(locB), eq(locD), eq(FLIGHT)))
                .thenReturn(List.of());

        List<RouteDto> routes = routeService.getRoutes(1L, 5L, MONDAY);

        assertThat(routes).isEmpty();
    }

    @Test
    void getRoutes_sameOriginAndDestination_throwsInvalidRequest() {
        assertThatThrownBy(() -> routeService.getRoutes(1L, 1L, MONDAY))
                .isInstanceOf(InvalidRequest.class)
                .hasMessageContaining("origin and destination");
    }

    @Test
    void getRoutes_directFlight_returnsSingleLegRoute() {
        Location from = Location.builder().id(1L).name("A").build();
        Location to = Location.builder().id(2L).name("B").build();
        Transportation flight = Transportation.builder()
                .id(100L)
                .type(FLIGHT)
                .from(from)
                .to(to)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(from));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(to));
        when(transportationRepository.findByFrom(from)).thenReturn(List.of(flight));
        when(transportationRepository.findByTo(to)).thenReturn(List.of(flight));
        when(transportationRepository.findByFromAndToAndType(eq(from), eq(to), eq(FLIGHT)))
                .thenReturn(List.of(flight));

        List<RouteDto> routes = routeService.getRoutes(1L, 2L, MONDAY);

        assertThat(routes).hasSize(1);
        assertThat(routes.get(0).getTransportations()).containsExactly(flight);
        assertThat(routes.get(0).getFlightIndex()).isZero();
    }

    @Test
    void getRoutes_busThenFlightTwoLegs_connectingLocations() {
        Location locA = Location.builder().id(1L).name("A").build();
        Location locB = Location.builder().id(2L).name("B").build();
        Location locC = Location.builder().id(3L).name("C").build();

        Transportation bus = Transportation.builder()
                .id(10L)
                .type(TransportationType.BUS)
                .from(locA)
                .to(locB)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        Transportation flightLeg = Transportation.builder()
                .id(11L)
                .type(FLIGHT)
                .from(locB)
                .to(locC)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(locA));
        when(locationRepository.findById(3L)).thenReturn(Optional.of(locC));
        when(transportationRepository.findByFrom(locA)).thenReturn(List.of(bus));
        when(transportationRepository.findByTo(locC)).thenReturn(List.of(flightLeg));
        when(transportationRepository.findByFromAndToAndType(eq(locA), eq(locC), eq(FLIGHT)))
                .thenReturn(List.of());

        List<RouteDto> routes = routeService.getRoutes(1L, 3L, MONDAY);

        assertThat(routes).hasSize(1);
        assertThat(routes.get(0).getTransportations()).containsExactly(bus, flightLeg);
        assertThat(routes.get(0).getFlightIndex()).isEqualTo(1);
    }

    @Test
    void getRoutes_noOperatingTransport_returnsEmptyRoutes() {
        Location from = Location.builder().id(1L).name("A").build();
        Location to = Location.builder().id(2L).name("B").build();
        Transportation wrongDay = Transportation.builder()
                .id(1L)
                .type(FLIGHT)
                .from(from)
                .to(to)
                .operatingDays(Day.toByte(List.of(Day.TUESDAY)))
                .build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(from));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(to));
        when(transportationRepository.findByFrom(from)).thenReturn(List.of(wrongDay));
        when(transportationRepository.findByTo(to)).thenReturn(List.of(wrongDay));
        when(transportationRepository.findByFromAndToAndType(eq(from), eq(to), eq(FLIGHT)))
                .thenReturn(List.of());

        List<RouteDto> routes = routeService.getRoutes(1L, 2L, MONDAY);

        assertThat(routes).isEmpty();
    }
}
