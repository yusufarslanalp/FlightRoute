package com.example.FlightRoute.dto;

import com.example.FlightRoute.model.TransportationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTransportationDto {
    @NotNull
    private TransportationType type;
    private List<String> days;
}