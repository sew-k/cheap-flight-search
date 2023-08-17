package com.kodilla.cheapflightsearch.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AuthenticationContext authenticationContext;

    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
    }

    public void logout() {
        authenticationContext.logout();
    }
}
