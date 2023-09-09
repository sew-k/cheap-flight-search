package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.trip.TripPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TripPlanRepository extends CrudRepository<TripPlan, Long> {
    @Override
    List<TripPlan> findAll();
    @Override
    TripPlan save(TripPlan tripPlan);
    @Override
    Optional<TripPlan> findById(Long id);
    @Override
    void deleteById(Long id);
    List<TripPlan> findByUser(User user);
}
