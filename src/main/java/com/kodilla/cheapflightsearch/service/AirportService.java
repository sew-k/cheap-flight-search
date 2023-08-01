package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.exception.AirportNotFoundException;
import com.kodilla.cheapflightsearch.domain.trip.Airport;
import com.kodilla.cheapflightsearch.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {
    private final AirportRepository airportRepository;
    public List<Airport> getAirports() {
        return airportRepository.findAll();
    }
    public Airport getAirport(Long id) throws AirportNotFoundException {
        return airportRepository.findById(id).orElseThrow(AirportNotFoundException::new);
    }
    public Airport getAirportByIata(String iata) throws AirportNotFoundException {
        return airportRepository.findByIataCode(iata).orElseThrow(AirportNotFoundException::new);
    }
    public void deleteAirport(Long id) throws AirportNotFoundException {
        if(airportRepository.findById(id).isPresent()) {
            airportRepository.deleteById(id);
        } else {
            throw new AirportNotFoundException();
        }
    }
    public void updateAirport(Long id, Airport airport) throws AirportNotFoundException {
        if(airportRepository.findById(id).isPresent()) {
            airportRepository.save(airport);
        } else {
            throw new AirportNotFoundException();
        }
    }
    public Airport createAirport(Airport airport) {
        return airportRepository.save(airport);
    }
    public List<Airport> getAirportsByCity(String city) {
        return airportRepository.findByCity(city);
    }
    public List<Airport> getAirportsByCountry(String country) {
        return airportRepository.findByCity(country);
    }
    public boolean checkIfAirportExists(String iataCode) {
        return airportRepository.findByIataCode(iataCode).isPresent();
    }

}
