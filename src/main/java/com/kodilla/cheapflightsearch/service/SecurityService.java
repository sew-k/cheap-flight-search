package com.kodilla.cheapflightsearch.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AuthenticationContext authenticationContext;

    public boolean isCurrentUserAllowed() {
        Optional<UserDetails> loggedUserDetails = Optional.ofNullable(getAuthenticatedUser());
        if (loggedUserDetails.isPresent()) {
            return (loggedUserDetails.get().isAccountNonExpired()
                    && loggedUserDetails.get().isAccountNonLocked()
                    && loggedUserDetails.get().isCredentialsNonExpired());
        } else {
            return false;
        }
    }

    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).orElse(null);
    }

    public void logout() {
        authenticationContext.logout();
    }
}
