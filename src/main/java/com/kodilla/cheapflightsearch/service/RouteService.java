package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.controller.RouteNotFoundException;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import com.kodilla.cheapflightsearch.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    public List<Route> getRoutes() {
        return routeRepository.findAll();
    }
    public Route getRoute(Long id) throws RouteNotFoundException {
        return routeRepository.findById(id).orElseThrow(RouteNotFoundException::new);
    }
    public void deleteRoute(Long id) throws RouteNotFoundException {
        if(routeRepository.findById(id).isPresent()) {
            routeRepository.deleteById(id);
        } else {
            throw new RouteNotFoundException();
        }
    }
    public void updateRoute(Long id, Route route) throws RouteNotFoundException {
        if(routeRepository.findById(id).isPresent()) {
            routeRepository.deleteById(id);
            routeRepository.save(route);
        } else {
            throw new RouteNotFoundException();
        }
    }
    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }
    public Route getRouteByOrigin(String origin) throws RouteNotFoundException {
        return routeRepository.findByOrigin(origin).orElseThrow(RouteNotFoundException::new);
    }
    public Route getRouteByDestination(String destination) throws RouteNotFoundException {
        return routeRepository.findByDestination(destination).orElseThrow(RouteNotFoundException::new);
    }
    public List<Route> getFavouriteRoutes() throws RouteNotFoundException {
        return routeRepository.findAll().stream()
                .filter(Route::isFavourite)
                .collect(Collectors.toList());
    }
}
