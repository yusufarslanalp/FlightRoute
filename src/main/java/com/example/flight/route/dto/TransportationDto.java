package com.example.flight.route.dto;

import java.util.List;

public record TransportationDto(Long id, String type, String from, String to, List<String> days) {}
