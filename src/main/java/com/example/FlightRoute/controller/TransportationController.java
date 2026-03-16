package com.example.FlightRoute.controller;


import com.example.FlightRoute.dto.CreateTransportationDto;
import com.example.FlightRoute.dto.UpdateTransportationDto;
import com.example.FlightRoute.model.Transportation;
import com.example.FlightRoute.service.TransportationService;
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
@RequestMapping( "transportation" )
@AllArgsConstructor
public class TransportationController {
    private final TransportationService transportationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Transportation> getTransportations(){
        return transportationService.getTransportations();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Transportation createTransportation(@Valid @RequestBody CreateTransportationDto createTransportationDto){
        return transportationService.saveTransportation(createTransportationDto);
    }

    @PutMapping("/{transportationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Transportation updateTransportation( @PathVariable @NotNull Long transportationId,
            @Valid @RequestBody UpdateTransportationDto updateTransportationDto){
        return transportationService.updateTransportation(transportationId, updateTransportationDto);
    }

    @DeleteMapping("/{transportationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLocation(@PathVariable @NotNull Long transportationId){
        transportationService.deleteTransportation(transportationId);
    }

}
