package com.example.FlightRoute.dto;

import com.example.FlightRoute.model.Transportation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDto implements Serializable {
    private static final long serialVersionUID = -8062776927256743554L;

    private int id;
    private int flightIndex;
    private List<Transportation> transportations;
}
