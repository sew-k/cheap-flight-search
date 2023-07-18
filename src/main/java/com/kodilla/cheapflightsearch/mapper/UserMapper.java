package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.User;
import com.kodilla.cheapflightsearch.dto.UserDto;
import com.kodilla.cheapflightsearch.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    @Autowired
    CalendarRepository calendarRepository;
    public User mapToUser(final UserDto userDto) {
        return new User(
                userDto.getUserId(),
                userDto.getUsername(),
                userDto.getEmail(),
//                calendarRepository.findById(userDto.getCalendarId())
                null        //TODO temporarily
        );
    }
    public UserDto mapToUserDto(final User user) {
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getCalendar().getCalendarId()
        );
    }
    public List<UserDto> mapToUserDtoList(final List<User> userList) {
        return userList.stream().map(this::mapToUserDto).collect(Collectors.toList());
    }
    public List<User> mapToUserList(final List<UserDto> userDtoList) {
        return userDtoList.stream().map(this::mapToUser).collect(Collectors.toList());
    }
}
