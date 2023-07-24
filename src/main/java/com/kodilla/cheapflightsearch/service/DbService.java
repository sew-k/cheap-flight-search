package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DbService {
    private final UserRepository userRepository;
}
