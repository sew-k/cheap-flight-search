package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.trip.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface RouteRepository extends CrudRepository<Route, Long> {
    @Override
    List<Route> findAll();
    @Override
    Route save(Route route);
    @Override
    Optional<Route> findById(Long id);
    @Override
    void deleteById(Long id);
    Optional<Route> findByOrigin(Airport origin);
    Optional<Route> findByDestination(Airport destination);
}
