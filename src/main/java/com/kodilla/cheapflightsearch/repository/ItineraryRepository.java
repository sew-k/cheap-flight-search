package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.skyscanner.Itinerary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
@Transactional
public interface ItineraryRepository extends CrudRepository<Itinerary, Long> {
    @Override
    List<Itinerary> findAll();
    @Override
    Itinerary save(Itinerary itinerary);
    @Override
    Optional<Itinerary> findById(Long id);
    @Override
    void deleteById(Long id);
}
