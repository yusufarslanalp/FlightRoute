package com.example.flight.route.dto;


import com.example.flight.route.model.Transportation;

import java.util.ArrayList;
import java.util.List;

public class RouteDtoConverter {
    private RouteDtoConverter() {}


    public static List<RouteDto> convert(List<List<Transportation>> routes){
        int id = 0;
        List<RouteDto> routeDtoList = new ArrayList<>();
        for(List<Transportation> route : routes){
            routeDtoList.add(
                new RouteDto(id, getFlightIndex(route), route)
            );
            id++;
        }
        return routeDtoList;
    }

    private static int getFlightIndex(List<Transportation> route){
        if(route.size() == 1){
            return 0;
        }
        if(route.size() == 3){
            return 1;
        }
        if (route.size() == 2){
            if(route.getFirst().isFlight()){
                return 0;
            }
            return 1;
        }
        return -1;
    }
}
