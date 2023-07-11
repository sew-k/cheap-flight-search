package com.kodilla.cheapflightsearch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @Column(name = "username")
    private String username;

    @NotNull
    @Column(name = "email")
    private String email;

    @OneToOne
    private Calendar calendar;

    public User(Long userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.calendar = new Calendar();
    }
}
