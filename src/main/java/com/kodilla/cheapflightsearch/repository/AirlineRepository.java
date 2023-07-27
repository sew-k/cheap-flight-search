package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.trip.Airline;
import com.kodilla.cheapflightsearch.domain.trip.Route;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AirlineRepository extends CrudRepository<Airline, Long> {
    @Override
    List<Airline> findAll();
    @Override
    Airline save(Airline airline);
    @Override
    Optional<Airline> findById(Long id);
    @Override
    void deleteById(Long id);
    Optional<Airline> findByAirlineName(String airlineName);
    Optional<Airline> findByCarrierId(String carrierId);
}
