package com.kodilla.cheapflightsearch.mapper;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.domain.user.UserDto;
import com.kodilla.cheapflightsearch.repository.CalendarRepository;
import com.kodilla.cheapflightsearch.service.ItineraryService;
import com.kodilla.cheapflightsearch.service.RouteService;
import com.kodilla.cheapflightsearch.service.TripPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    @Autowired
    CalendarRepository calendarRepository;
    @Autowired
    ItineraryService itineraryService;
    @Autowired
    RouteService routeService;
    @Autowired
    TripPlanService tripPlanService;

    public User mapToUser(final UserDto userDto) {
        return new User(
                userDto.getUserId(),
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getRole(),
                userDto.getPassword(),
                calendarRepository.findById(userDto.getCalendarId()).orElse(new Calendar()),
                tripPlanService.getTripPlansByUserId(userDto.getUserId()),
                routeService.getRoutesByUserId(userDto.getUserId())
        );
    }

    public UserDto mapToUserDto(final User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .password(user.getPassword())
                .calendarId(user.getCalendar().getCalendarId())
                .build();
    }

    public List<UserDto> mapToUserDtoList(final List<User> userList) {
        return userList.stream().map(this::mapToUserDto).collect(Collectors.toList());
    }

    public List<User> mapToUserList(final List<UserDto> userDtoList) {
        return userDtoList.stream().map(this::mapToUser).collect(Collectors.toList());
    }
}
