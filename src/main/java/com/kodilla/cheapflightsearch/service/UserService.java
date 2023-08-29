package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.exception.UserNotFoundException;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public static User currentUser;
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public User getUser(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
    public boolean checkIfUserExists(User user) {
        if ((userRepository.existsByUsername(user.getUsername())) || (userRepository.existsByEmail(user.getEmail()))) {
            return true;
        } else {
            return false;
        }
    }
    public void deleteUser(Long id)  throws UserNotFoundException {
        if(userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException();
        }
    }
    public User updateUser(Long id, User user) throws UserNotFoundException {
        if(userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    public User createUser(User user) {
        if (!checkIfUserExists(user)) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            return userRepository.save(user);
        } else return null;
    }

    public User getUserByName(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserService.currentUser = currentUser;
    }

    public boolean checkLogin(String username, String password) {
        Optional<User> userToCheck = userRepository.findByUsername(username);
        if (userToCheck.isPresent() && passwordEncoder.matches(password, userToCheck.get().getPassword())) {
            return true;
        } else {
            return false;
        }
    }
}
