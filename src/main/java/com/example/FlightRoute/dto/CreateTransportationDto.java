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
public class CreateTransportationDto {
    @NotNull
    private Long fromId;
    @NotNull
    private Long toId;
    @NotNull
    private TransportationType type;
    private List<String> days;
}