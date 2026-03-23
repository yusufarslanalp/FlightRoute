package com.example.flight.route.dto;

import com.example.flight.route.model.Transportation;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record RouteDto(int id, int flightIndex, List<Transportation> transportations) implements Serializable {
    @Serial
    private static final long serialVersionUID = -8062776927256743554L;
}
