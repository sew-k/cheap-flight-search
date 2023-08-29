package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.calendar.HolidayPlan;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.domain.user.UserDto;
import com.kodilla.cheapflightsearch.domain.user.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTestSuite {

    @Autowired
    UserMapper userMapper;

    @Test
    void testMapToUser() {
        //Given
        UserDto userDto = UserDto.builder()
                .userId(1L)
                .username("Name")
                .email("email")
                .build();

        //When
        User resultUser = userMapper.mapToUser(userDto);

        //Then
        assertEquals(userDto.getUserId(), resultUser.getUserId());
        assertEquals(userDto.getUsername(), resultUser.getUsername());
        assertEquals(userDto.getEmail(), resultUser.getEmail());
    }

    @Test
    void testMapToUserDto() {
        //Given
        Calendar calendar = new Calendar(1L, List.of(new HolidayPlan()));
        User user = new User(1L, "Name", "email", UserRole.USER, "password", calendar);

        //When
        UserDto resultUserDto = userMapper.mapToUserDto(user);

        //Then
        assertEquals(user.getUserId(), resultUserDto.getUserId());
        assertEquals(user.getUsername(), resultUserDto.getUsername());
        assertEquals(user.getEmail(), resultUserDto.getEmail());
    }

    @Test
    void testMapToUserDtoList() {
        //Given
        Calendar calendar2 = new Calendar(2L, List.of(new HolidayPlan()));
        User user2 = new User(2L, "Name", "email", UserRole.USER, "password", calendar2);
        Calendar calendar3 = new Calendar(3L, List.of(new HolidayPlan()));
        User user3 = new User(3L, "Name", "email", UserRole.USER, "password", calendar3);
        List<User> userList = List.of(
                user2,
                user3
        );

        //When
        List<UserDto> resultUserDtoList = userMapper.mapToUserDtoList(userList);

        //Then
        assertFalse(resultUserDtoList.isEmpty());
        assertEquals(userList.size(), resultUserDtoList.size());
        assertEquals(userList.get(0).getUserId(), resultUserDtoList.get(0).getUserId());
        assertEquals(userList.get(0).getUsername(), resultUserDtoList.get(0).getUsername());
        assertEquals(userList.get(0).getEmail(), resultUserDtoList.get(0).getEmail());
    }

    @Test
    void testMapToUserList() {
        //Given
        List<UserDto> userDtoList = List.of(
                UserDto.builder()
                        .userId(1L)
                        .username("Name1")
                        .email("email1")
                        .role(UserRole.USER)
                        .build(),
                UserDto.builder()
                        .userId(2L)
                        .username("Name2")
                        .email("email2")
                        .role(UserRole.USER)
                        .build()
        );

        //When
        List<User> resultUserList = userMapper.mapToUserList(userDtoList);

        //Then
        assertFalse(resultUserList.isEmpty());
        assertEquals(userDtoList.size(), resultUserList.size());
        assertEquals(userDtoList.get(0).getUserId(), resultUserList.get(0).getUserId());
        assertEquals(userDtoList.get(0).getUsername(), resultUserList.get(0).getUsername());
        assertEquals(userDtoList.get(0).getEmail(), resultUserList.get(0).getEmail());
    }
}