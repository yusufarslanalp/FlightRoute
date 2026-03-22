package com.example.FlightRoute.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportationDto {
    private Long id;
    private String type;
    private String from;
    private String to;
    private List<String> days;
}
