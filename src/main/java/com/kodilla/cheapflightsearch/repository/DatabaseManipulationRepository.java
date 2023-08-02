package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.watcher.DatabaseManipulation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface DatabaseManipulationRepository extends CrudRepository<DatabaseManipulation, Long> {
    @Override
    List<DatabaseManipulation> findAll();
    @Override
    DatabaseManipulation save(DatabaseManipulation catabaseManipulation);
    @Override
    Optional<DatabaseManipulation> findById(Long id);
    @Override
    void deleteById(Long id);
    @Override
    boolean existsById(Long id);
}
