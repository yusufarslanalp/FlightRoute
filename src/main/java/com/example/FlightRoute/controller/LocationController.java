package com.example.FlightRoute.controller;

import com.example.FlightRoute.model.Location;
import com.example.FlightRoute.service.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping( "location" )
@AllArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations(){
        return locationService.getLocations();
    }

    @PostMapping
    public Location createLocation(@RequestBody @Valid Location location){
        return locationService.saveLocation(location);
    }

    @PutMapping
    public Location updateLocation(@RequestBody @Valid Location location){
        return locationService.saveLocation(location);
    }

    @DeleteMapping("/{locationId}")
    public void deleteLocation(@PathVariable @NotNull Long locationId){
        locationService.deleteLocation(locationId);
    }
}