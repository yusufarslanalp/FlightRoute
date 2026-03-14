package com.example.FlightRoute.controller;


import com.example.FlightRoute.dto.CreateTransportationDto;
import com.example.FlightRoute.dto.UpdateTransportationDto;
import com.example.FlightRoute.model.Transportation;
import com.example.FlightRoute.service.TransportationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping( "transportation" )
@AllArgsConstructor
public class TransportationController {
    private final TransportationService transportationService;

    @GetMapping
    public List<Transportation> getTransportations(){
        return transportationService.getTransportations();
    }

    @PostMapping
    public Transportation createTransportation(@Valid @RequestBody CreateTransportationDto createTransportationDto){
        return transportationService.saveTransportation(createTransportationDto);
    }

    @PutMapping("/{transportationId}")
    public Transportation updateTransportation( @PathVariable @NotNull Long transportationId,
            @Valid @RequestBody UpdateTransportationDto updateTransportationDto){
        return transportationService.updateTransportation(transportationId, updateTransportationDto);
    }

    @DeleteMapping("/{transportationId}")
    public void deleteLocation(@PathVariable @NotNull Long transportationId){
        transportationService.deleteTransportation(transportationId);
    }

}
