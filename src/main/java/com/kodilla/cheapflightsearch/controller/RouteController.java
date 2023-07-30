package com.kodilla.cheapflightsearch.controller;

import com.kodilla.cheapflightsearch.domain.trip.RouteDto;
import com.kodilla.cheapflightsearch.mapper.RouteMapper;
import com.kodilla.cheapflightsearch.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cheapflightsearch/routes")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RouteController {
    private final RouteService routeService;
    private final RouteMapper routeMapper;
    @GetMapping()
    public ResponseEntity<List<RouteDto>> getRoutes() {
        return ResponseEntity.ok(routeMapper.mapToRouteDtoList(routeService.getRoutes()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<RouteDto> getRouteById(@PathVariable("id") long id) throws RouteNotFoundException {
        return ResponseEntity.ok(routeMapper.mapToRouteDto(routeService.getRoute(id)));
    }
//    @GetMapping("/route/{origin}")
//    public ResponseEntity<RouteDto> getRouteByOrigin(@PathVariable("origin") String origin) throws RouteNotFoundException {
//        return ResponseEntity.ok(routeMapper.mapToRouteDto(routeService.getRouteByOrigin(origin)));
//    }
//    @GetMapping("/route/{destination}")
//    public ResponseEntity<RouteDto> getRouteByDestination(@PathVariable("destination") String destination) throws RouteNotFoundException {
//        return ResponseEntity.ok(routeMapper.mapToRouteDto(routeService.getRouteByDestination(destination)));
//    }
    @PutMapping(path="/updateRoute/{id}", consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RouteDto> updateRoute(@PathVariable("id") long id, @RequestBody RouteDto routeDto) throws RouteNotFoundException {
        routeService.updateRoute(id, routeMapper.mapToRoute(routeDto));
        return ResponseEntity.ok(routeMapper.mapToRouteDto(routeService.getRoute(routeDto.getRouteId())));
    }
    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createRoute(@RequestBody RouteDto routeDto) {
        routeService.createRoute(routeMapper.mapToRoute(routeDto));
        return ResponseEntity.ok().build();
    }
}
