package com.kodilla.cheapflightsearch.repository;

import com.kodilla.cheapflightsearch.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class UserRepositoryTestSuite {
    @Autowired
    UserRepository userRepository;
    private static List<User> usersTestList;
    private static List<Long> usersIdsTestList = new ArrayList<>();

    @BeforeAll
    static void setUp() {
        usersTestList = List.of(
                new User("TestUser1", "testUser1@test.com", "password"),
                new User("TestUser2", "testUser2@test.com", "password"),
                new User("TestUser3", "testUser3@test.com", "password"),
                new User("TestUser4", "testUser4@test.com", "password")
        );
    }
    @Test
    void testFindAll() {
        //Given
        for (User user: usersTestList) {
            userRepository.save(user);
            usersIdsTestList.add(user.getUserId());
        }

        //When
        List<User> retrievedUsersList = userRepository.findAll();

        //Then
        Assertions.assertEquals(usersTestList.size(), retrievedUsersList.size());

        //CleanUp
        for (Long id: usersIdsTestList) {
            userRepository.deleteById(id);
        }
    }
    @Test
    void testSave() {
        //Given & When
        User user = new User("TestUser5", "testUser5@test.com", "password");
        userRepository.save(user);
        Long id = user.getUserId();

        //Then
        Assertions.assertTrue(userRepository.findById(id).isPresent());
        Assertions.assertEquals(user.getUsername(),
                userRepository.findById(id).get().getUsername());

        //CleanUp
        userRepository.deleteById(id);
    }
    @Test
    void testFindByIdWhenExisting() {
        //Given
        User user = new User("TestUser6", "testUser6@test.com", "password");
        userRepository.save(user);
        Long id = user.getUserId();

        //When
        User retrievedUser = userRepository.findById(id).orElse(null);

        //Then
        Assertions.assertEquals(user, retrievedUser);

        //CleanUp
        userRepository.deleteById(id);
    }
    @Test
    void testFindByIdWhenNotExisting() {
        //Given & When & Then
        Assertions.assertFalse(userRepository.findById(-1L).isPresent());
    }
    @Test
    void testFindByUsernameWhenExisting() {
        //Given
        User user = new User("John", "john@test.com", "password");
        userRepository.save(user);
        Long id = user.getUserId();

        //When
        User retrievedUser = userRepository.findByUsername(user.getUsername()).orElse(null);

        //Then
        Assertions.assertEquals(user, retrievedUser);

        //CleanUp
        userRepository.deleteById(id);
    }
    @Test
    void testFindByUsernameWhenNotExisting() {
        //Given & When & Then
        Assertions.assertFalse(userRepository.findByUsername("NameUserNotExisting").isPresent());
    }
    @Test
    void testFindByEmailWhenExisting() {
        //Given
        User user = new User("John", "john@test.com", "password");
        userRepository.save(user);
        Long id = user.getUserId();

        //When
        User retrievedUser = userRepository.findByEmail(user.getEmail()).orElse(null);

        //Then
        Assertions.assertEquals(user, retrievedUser);

        //CleanUp
        userRepository.deleteById(id);
    }
    @Test
    void testFindByEmailWhenNotExisting() {
        //Given & When & Then
        Assertions.assertFalse(userRepository.findByUsername("EmailUserNotExisting").isPresent());
    }
    @Test
    void testDeleteById() {
        //Given
        User user = new User("TestUser7", "testUser7@test.com", "password");
        userRepository.save(user);
        Long id = user.getUserId();

        //When
        userRepository.deleteById(id);

        //Then
        Assertions.assertFalse(userRepository.findById(id).isPresent());

        //CleanUp
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {

        }
    }
}