package com.example.FlightRoute.controller;


import com.example.FlightRoute.dto.RouteDto;
import com.example.FlightRoute.service.RouteService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("route")
@AllArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','AGENCY')")
    public List<RouteDto> getAllLocations(@RequestParam @NotNull Long fromId,
                                          @RequestParam @NotNull Long toId) {
        return routeService.getRoutes(fromId, toId);
    }
}
