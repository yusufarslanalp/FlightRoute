package com.example.FlightRoute.controller;


import com.example.FlightRoute.dto.RouteDto;
import com.example.FlightRoute.service.RouteService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("route")
@AllArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public List<RouteDto> getAllLocations(@RequestParam @NotNull Long fromId,
                                          @RequestParam @NotNull Long toId) {
        return routeService.getRoutes(fromId, toId);
    }
}
