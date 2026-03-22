package com.example.FlightRoute.dto;

import com.example.FlightRoute.model.TransportationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTransportationDto {
    @NotNull(message = "Transportation type is required")
    private TransportationType type;
    
    @Size(max = 7, message = "Days list cannot contain more than 7 elements")
    private List<String> days;
}