package com.example.flight.route.dto;

import com.example.flight.route.model.TransportationType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateTransportationDto(
    @NotNull(message = "From location ID is required")
    @Positive(message = "From location ID must be positive")
    Long fromId,
    
    @NotNull(message = "To location ID is required")
    @Positive(message = "To location ID must be positive")
    Long toId,
    
    @NotNull(message = "Transportation type is required")
    TransportationType type,
    
    @Size(max = 7, message = "Days list cannot contain more than 7 elements")
    List<String> days
) {}