package com.kodilla.cheapflightsearch.domain.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    private long userId;
    private String username;
    private String email;
    private UserRole role;
    private long calendarId;
}
