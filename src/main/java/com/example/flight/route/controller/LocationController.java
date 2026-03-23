package com.example.flight.route.controller;

import com.example.flight.route.model.Location;
import com.example.flight.route.service.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping( "location" )
@AllArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','AGENCY')")
    public List<Location> getAllLocations(){
        return locationService.getLocations();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Location createLocation(@RequestBody @Valid Location location){
        return locationService.saveLocation(location);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Location updateLocation(@RequestBody @Valid Location location){
        return locationService.saveLocation(location);
    }

    @DeleteMapping("/{locationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLocation(@PathVariable @NotNull Long locationId){
        locationService.deleteLocation(locationId);
    }
}