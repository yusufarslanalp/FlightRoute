package com.example.FlightRoute.service;


import com.example.FlightRoute.dto.RouteDto;
import com.example.FlightRoute.dto.RouteDtoConverter;
import com.example.FlightRoute.exception.InvalidRequest;
import com.example.FlightRoute.model.Location;
import com.example.FlightRoute.model.Transportation;
import com.example.FlightRoute.model.TransportationType;
import com.example.FlightRoute.repository.LocationRepository;
import com.example.FlightRoute.repository.TransportationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class RouteService {
    private final TransportationRepository transportationRepository;
    private final LocationRepository locationRepository;

    public List<RouteDto> getRoutes(Long fromId, Long toId) {
        if (fromId.equals(toId)) {
            throw new InvalidRequest("origin and destination can not be same of a route");
        }

        Location from = locationRepository.findById(fromId).get();
        Location to = locationRepository.findById(toId).get();

        List<Transportation> firstTransportations = transportationRepository.findByFrom(from);
        List<Transportation> lastTransportations = transportationRepository.findByTo(to);

        List<List<Transportation>> routes = new ArrayList<>();

        addRouteForDirectFlight(routes, from, to);
        addRoutesFor2Transportation(routes, firstTransportations, lastTransportations);
        addRoutesFor3Transportation(routes, firstTransportations, lastTransportations);


        return RouteDtoConverter.convert(routes);
    }

    private void addRouteForDirectFlight(List<List<Transportation>> routes, Location from, Location to) {
        List<Transportation> directFlights = transportationRepository
                .findByFromAndToAndType(from, to, TransportationType.FLIGHT);
        if (directFlights.size() > 0) {
            routes.add(
                    Arrays.asList(directFlights.get(0))
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
                                             List<Transportation> lastTransportations) {
        for (Transportation firsTransportation : firstTransportations) {
            for (Transportation lastTransportation : lastTransportations) {

                if(firsTransportation.isFlight() || lastTransportation.isFlight()){
                    continue;
                }

                List<Transportation> connector = transportationRepository
                        .findByFromAndToAndType(firsTransportation.getTo(),
                                lastTransportation.getFrom(), TransportationType.FLIGHT);

                if (connector.size() > 0) {
                    routes.add(
                            Arrays.asList(
                                    firsTransportation,
                                    connector.get(0),
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
}
