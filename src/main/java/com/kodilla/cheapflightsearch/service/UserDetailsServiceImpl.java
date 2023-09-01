package com.kodilla.cheapflightsearch.service;

import com.kodilla.cheapflightsearch.domain.user.User;
import com.kodilla.cheapflightsearch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    private List<GrantedAuthority> authorities = new ArrayList<>();
    private GrantedAuthority authority;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        } else {
            authorities.add(getUserSimpleGrantedAuthority(user));
            return new org.springframework.security.core.userdetails.User(
                    user.get().getUsername(),
                    user.get().getPassword(),
                    authorities
            );
        }
    }

    private SimpleGrantedAuthority getUserSimpleGrantedAuthority(Optional<User> user) {
        return new SimpleGrantedAuthority("ROLE_" + user.get().getRole().toString());
    }
}

