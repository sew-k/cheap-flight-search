package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.trip.Airport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AirportRepository extends CrudRepository<Airport, Long> {
    @Override
    List<Airport> findAll();
    @Override
    Airport save(Airport airport);
    @Override
    Optional<Airport> findById(Long id);
    @Override
    void deleteById(Long id);
    List<Airport> findByCountry(String country);
    List<Airport> findByCity(String city);
    Optional<Airport> findByIataCode(String iataCode);
}
