package com.kodilla.cheapflightsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDto {
    private long userId;
    private String username;
    private String email;
    private long calendarId;

    public UserDto(Long userId, String username, String email, Long calendarId) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.calendarId = calendarId;
    }
}
