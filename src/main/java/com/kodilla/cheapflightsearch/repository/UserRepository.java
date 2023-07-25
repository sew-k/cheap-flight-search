package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Long> {
    @Override
    List<User> findAll();
    @Override
    User save(User user);
    @Override
    Optional<User> findById(Long id);
    @Override
    void deleteById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
