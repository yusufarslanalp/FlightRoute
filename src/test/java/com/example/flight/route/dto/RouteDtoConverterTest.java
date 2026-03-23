package com.example.flight.route.dto;

import com.example.flight.route.model.Day;
import com.example.flight.route.model.Location;
import com.example.flight.route.model.Transportation;
import com.example.flight.route.model.TransportationType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RouteDtoConverterTest {

    @Test
    void convert_emptyInput_returnsEmptyList() {
        assertThat(RouteDtoConverter.convert(List.of())).isEmpty();
    }

    @Test
    void convert_singleFlight_flightIndexZero() {
        Location a = Location.builder().id(1L).name("A").build();
        Location b = Location.builder().id(2L).name("B").build();
        Transportation flight = Transportation.builder()
                .id(1L)
                .type(TransportationType.FLIGHT)
                .from(a)
                .to(b)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();

        List<RouteDto> result = RouteDtoConverter.convert(List.of(List.of(flight)));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isZero();
        assertThat(result.get(0).getFlightIndex()).isZero();
        assertThat(result.get(0).getTransportations()).containsExactly(flight);
    }

    @Test
    void convert_twoLegs_flightFirst_flightIndexZero() {
        Location a = Location.builder().id(1L).name("A").build();
        Location b = Location.builder().id(2L).name("B").build();
        Location c = Location.builder().id(3L).name("C").build();
        Transportation flight = Transportation.builder()
                .id(1L)
                .type(TransportationType.FLIGHT)
                .from(a)
                .to(b)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        Transportation bus = Transportation.builder()
                .id(2L)
                .type(TransportationType.BUS)
                .from(b)
                .to(c)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();

        List<RouteDto> result = RouteDtoConverter.convert(List.of(List.of(flight, bus)));

        assertThat(result.get(0).getFlightIndex()).isZero();
    }

    @Test
    void convert_twoLegs_flightSecond_flightIndexOne() {
        Location a = Location.builder().id(1L).name("A").build();
        Location b = Location.builder().id(2L).name("B").build();
        Location c = Location.builder().id(3L).name("C").build();
        Transportation bus = Transportation.builder()
                .id(1L)
                .type(TransportationType.BUS)
                .from(a)
                .to(b)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        Transportation flight = Transportation.builder()
                .id(2L)
                .type(TransportationType.FLIGHT)
                .from(b)
                .to(c)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();

        List<RouteDto> result = RouteDtoConverter.convert(List.of(List.of(bus, flight)));

        assertThat(result.get(0).getFlightIndex()).isEqualTo(1);
    }

    @Test
    void convert_threeLegs_flightIndexOne() {
        Location a = Location.builder().id(1L).name("A").build();
        Location b = Location.builder().id(2L).name("B").build();
        Location c = Location.builder().id(3L).name("C").build();
        Location d = Location.builder().id(4L).name("D").build();
        Transportation first = Transportation.builder()
                .id(1L)
                .type(TransportationType.BUS)
                .from(a)
                .to(b)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        Transportation mid = Transportation.builder()
                .id(2L)
                .type(TransportationType.FLIGHT)
                .from(b)
                .to(c)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();
        Transportation last = Transportation.builder()
                .id(3L)
                .type(TransportationType.SUBWAY)
                .from(c)
                .to(d)
                .operatingDays(Day.toByte(List.of(Day.MONDAY)))
                .build();

        List<RouteDto> result = RouteDtoConverter.convert(List.of(List.of(first, mid, last)));

        assertThat(result.get(0).getFlightIndex()).isEqualTo(1);
        assertThat(result.get(0).getId()).isZero();
    }
}
