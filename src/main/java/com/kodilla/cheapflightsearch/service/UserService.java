package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.controller.UserNotFoundException;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public void deleteUser(Long id)  {
        if(userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException();
        }
    }

    public void updateUser(Long id, User user)  {
        if(userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            userRepository.save(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }
}
