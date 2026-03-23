package com.example.flight.route.service;


import com.example.flight.route.dto.RouteDto;
import com.example.flight.route.dto.RouteDtoConverter;
import com.example.flight.route.exception.InvalidRequest;
import com.example.flight.route.model.Day;
import com.example.flight.route.model.Location;
import com.example.flight.route.model.Transportation;
import com.example.flight.route.model.TransportationType;
import com.example.flight.route.repository.LocationRepository;
import com.example.flight.route.repository.TransportationRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.flight.route.config.CacheConfig.FIVE_MIN;


@Component
@AllArgsConstructor
public class RouteService {
    private final TransportationRepository transportationRepository;
    private final LocationRepository locationRepository;

    @Cacheable(value = FIVE_MIN, key = "#fromId + '-' + #toId + '-' + #date")
    public List<RouteDto> getRoutes(Long fromId, Long toId, LocalDate date) {
        if (fromId.equals(toId)) {
            throw new InvalidRequest("origin and destination can not be same of a route");
        }

        Location from = locationRepository.findById(fromId).get();
        Location to = locationRepository.findById(toId).get();

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Day day = getDayFromDayOfWeek(dayOfWeek);

        List<Transportation> firstTransportations = filterByOperatingDay(transportationRepository.findByFrom(from), day);
        List<Transportation> lastTransportations = filterByOperatingDay(transportationRepository.findByTo(to), day);

        List<List<Transportation>> routes = new ArrayList<>();

        addRouteForDirectFlight(routes, from, to, day);
        addRoutesFor2Transportation(routes, firstTransportations, lastTransportations);
        addRoutesFor3Transportation(routes, firstTransportations, lastTransportations, day);


        return RouteDtoConverter.convert(routes);
    }

    private void addRouteForDirectFlight(List<List<Transportation>> routes, Location from, Location to, Day day) {
        List<Transportation> directFlights = filterByOperatingDay(
                transportationRepository.findByFromAndToAndType(from, to, TransportationType.FLIGHT), day);
        if (!directFlights.isEmpty()) {
            routes.add(
                    Collections.singletonList(directFlights.getFirst())
            );
        }
    }

    private void addRoutesFor2Transportation(List<List<Transportation>> routes,
                                             List<Transportation> firstTransportations, List<Transportation> lastTransportations) {

        for (Transportation firsTransportation : firstTransportations) {
            for (Transportation lastTransportation : lastTransportations) {

                if(!isContainsOnlyOneFlight(firsTransportation, lastTransportation)){
                    continue;
                }

                if (firsTransportation.isThereChange(lastTransportation)) {
                    routes.add(
                            Arrays.asList(
                                    firsTransportation,
                                    lastTransportation
                            )
                    );
                }
            }
        }
    }

    private void addRoutesFor3Transportation(List<List<Transportation>> routes,
                                             List<Transportation> firstTransportations,
                                             List<Transportation> lastTransportations, Day day) {
        for (Transportation firsTransportation : firstTransportations) {
            for (Transportation lastTransportation : lastTransportations) {

                if(firsTransportation.isFlight() || lastTransportation.isFlight()){
                    continue;
                }

                List<Transportation> connector = filterByOperatingDay(
                        transportationRepository.findByFromAndToAndType(firsTransportation.getTo(),
                                lastTransportation.getFrom(), TransportationType.FLIGHT), day);

                if (!connector.isEmpty()) {
                    routes.add(
                            Arrays.asList(
                                    firsTransportation,
                                    connector.getFirst(),
                                    lastTransportation
                            )
                    );
                }
            }
        }
    }

    private boolean isContainsOnlyOneFlight(Transportation first, Transportation second){
        if(first.isFlight() && !second.isFlight()){
            return true;
        }
        if(!first.isFlight() && second.isFlight()){
            return true;
        }
        return false;
    }

    private Day getDayFromDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return Day.MONDAY;
            case TUESDAY: return Day.TUESDAY;
            case WEDNESDAY: return Day.WEDNESDAY;
            case THURSDAY: return Day.THURSDAY;
            case FRIDAY: return Day.FRIDAY;
            case SATURDAY: return Day.SATURDAY;
            case SUNDAY: return Day.SUNDAY;
            default: throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        }
    }

    private List<Transportation> filterByOperatingDay(List<Transportation> transportations, Day day) {
        return transportations.stream()
                .filter(t -> (t.getOperatingDays() & day.getBitmask()) != 0)
                .toList();
    }
}
