package com.example.flight.route.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DayTest {

    @Test
    void toByte_singleDay_matchesBitmask() {
        byte b = Day.toByte(List.of(Day.MONDAY));
        assertThat(b).isEqualTo(Day.MONDAY.getBitmask());
    }

    @Test
    void toByte_multipleDays_combinesBitmasks() {
        byte b = Day.toByte(List.of(Day.MONDAY, Day.WEDNESDAY));
        assertThat(b & Day.MONDAY.getBitmask()).isNotZero();
        assertThat(b & Day.WEDNESDAY.getBitmask()).isNotZero();
        assertThat(b & Day.FRIDAY.getBitmask()).isZero();
    }

    @Test
    void fromByteToStringList_roundTrip() {
        List<Day> days = List.of(Day.TUESDAY, Day.THURSDAY);
        byte packed = Day.toByte(days);
        assertThat(Day.fromByteToStringList(packed)).containsExactlyInAnyOrder("TUESDAY", "THURSDAY");
    }
}
