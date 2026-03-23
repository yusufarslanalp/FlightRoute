package com.example.flight.route.model;

import lombok.Getter;
import java.util.List;
import java.util.stream.Stream;

@Getter
public enum Day {

    MONDAY(1, (byte) 0b00000001),
    TUESDAY(2, (byte) 0b00000010),
    WEDNESDAY(3, (byte) 0b00000100),
    THURSDAY(4, (byte) 0b00001000),
    FRIDAY(5, (byte) 0b00010000),
    SATURDAY(6, (byte) 0b00100000),
    SUNDAY(0, (byte) 0b01000000);

    private final int dayIndex;
    private final byte bitmask;

    Day(int dayIndex, byte bitmask) {
        this.dayIndex = dayIndex;
        this.bitmask = bitmask;
    }

    public static byte toByte(List<Day> days) {
        byte operatingDays = 0;
        for (Day day : days) {
            operatingDays |= day.getBitmask();
        }
        return operatingDays;
    }

    public static List<String> fromByteToStringList(byte operatingDays) {
        return Stream.of(values())
                .filter(day -> (operatingDays & day.getBitmask()) != 0)
                .map(Enum::name)
                .toList();
    }
}