package com.kodilla.cheapflightsearch.domain.user;

import com.kodilla.cheapflightsearch.domain.calendar.Calendar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "user_id", unique = true)
    private Long userId;
    @NotNull
    @Column(name = "username", unique = true)
    private String username;
    @NotNull
    @Column(name = "email", unique = true)
    private String email;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "calendar_id")
//    private Calendar calendar;
//    public User(String username, String email, Long calendarId) {
//        this.username = username;
//        this.email = email;
//
//    }
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}