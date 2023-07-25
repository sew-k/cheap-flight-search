package com.kodilla.cheapflightsearch.domain.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private long userId;
    private String username;
    private String email;
    private long calendarId;

    public UserDto(Long userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
    public UserDto(String username, String email, Long calendarId) {
        this.username = username;
        this.email = email;
        this.calendarId = calendarId;
    }
    public UserDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
