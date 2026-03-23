package com.example.flight.route.service;



import com.example.flight.route.dto.CreateTransportationDto;
import com.example.flight.route.dto.TransportationDto;
import com.example.flight.route.dto.UpdateTransportationDto;
import com.example.flight.route.exception.InvalidRequest;
import com.example.flight.route.model.Day;
import com.example.flight.route.model.Location;
import com.example.flight.route.model.Transportation;
import com.example.flight.route.repository.TransportationRepository;
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

    public TransportationDto saveTransportation(CreateTransportationDto createTransportationDto){
        if (createTransportationDto.getFromId()
                .equals(createTransportationDto.getToId())) {
            throw new InvalidRequest("origin and destination can not be same of a transportation");
        }

        List<Day> days = createTransportationDto.getDays().stream()
                .map(Day::valueOf).toList();

        Location from = locationService.findById(createTransportationDto.getFromId());
        Location to = locationService.findById(createTransportationDto.getToId());
        Transportation transportation = transportationRepository.save(Transportation.builder()
                .from(from)
                .to(to).type(createTransportationDto.getType())
                .operatingDays(Day.toByte(days))
                .build());

        return new TransportationDto(
                transportation.getId(),
                transportation.getType().name(),
                transportation.getFrom().getName(),
                transportation.getTo().getName(),
                Day.fromByteToStringList(transportation.getOperatingDays())
        );
    }

    public TransportationDto updateTransportation(Long transportationId, UpdateTransportationDto updateTransportationDto){
        Transportation transportation = transportationRepository.findById(transportationId).get();
        transportation.setType(updateTransportationDto.getType());
        transportation.setOperatingDays(Day.toByte(updateTransportationDto.getDays().stream()
                .map(Day::valueOf).toList()));
        transportationRepository.save(transportation);

        return new TransportationDto(
                transportation.getId(),
                transportation.getType().name(),
                transportation.getFrom().getName(),
                transportation.getTo().getName(),
                Day.fromByteToStringList(transportation.getOperatingDays())
        );
    }

    public void deleteTransportation(Long transportationId){
        transportationRepository.deleteById(transportationId);
    }

}
