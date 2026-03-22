package com.example.FlightRoute.service;



import com.example.FlightRoute.dto.CreateTransportationDto;
import com.example.FlightRoute.dto.TransportationDto;
import com.example.FlightRoute.dto.UpdateTransportationDto;
import com.example.FlightRoute.exception.InvalidRequest;
import com.example.FlightRoute.model.Day;
import com.example.FlightRoute.model.Location;
import com.example.FlightRoute.model.Transportation;
import com.example.FlightRoute.repository.TransportationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class TransportationService {
    private final TransportationRepository transportationRepository;
    private final LocationService locationService;


    public List<TransportationDto> getTransportations(){
        return transportationRepository.findAll().stream()
                .map(transportation -> new TransportationDto(
                        transportation.getId(),
                        transportation.getType().name(),
                        transportation.getFrom().getName(),
                        transportation.getTo().getName(),
                        Day.fromByteToStringList(transportation.getOperatingDays())
                ))
                .toList();
    }

    public Transportation saveTransportation(CreateTransportationDto createTransportationDto){
        if (createTransportationDto.getFromId()
                .equals(createTransportationDto.getToId())) {
            throw new InvalidRequest("origin and destination can not be same of a transportation");
        }

        List<Day> days = createTransportationDto.getDays().stream()
                .map(Day::valueOf).toList();

        Location from = locationService.findById(createTransportationDto.getFromId());
        Location to = locationService.findById(createTransportationDto.getToId());
        return transportationRepository.save(Transportation.builder()
                .from(from)
                .to(to).type(createTransportationDto.getType())
                .operatingDays(Day.toByte(days))
                .build());
    }

    public Transportation updateTransportation(Long transportationId, UpdateTransportationDto updateTransportationDto){
        Transportation transportation = transportationRepository.findById(transportationId).get();
        transportation.setType(updateTransportationDto.getType());
        transportation.setOperatingDays(Day.toByte(updateTransportationDto.getDays().stream()
                .map(Day::valueOf).toList()));
        transportationRepository.save(transportation);
        return transportation;
    }

    public void deleteTransportation(Long transportationId){
        transportationRepository.deleteById(transportationId);
    }

}
