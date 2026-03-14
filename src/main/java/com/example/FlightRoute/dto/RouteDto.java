package com.example.FlightRoute.dto;

import com.example.FlightRoute.model.Transportation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDto {
    private int id;
    private int flightIndex;
    private List<Transportation> transportations;
}
