package com.example.flight.route.dto;

import com.example.flight.route.model.TransportationType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateTransportationDto(
    @NotNull(message = "Transportation type is required")
    TransportationType type,
    
    @Size(max = 7, message = "Days list cannot contain more than 7 elements")
    List<String> days
) {}