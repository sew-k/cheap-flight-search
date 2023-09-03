package com.kodilla.cheapflightsearch.config;

import com.kodilla.cheapflightsearch.service.UserDetailsServiceImpl;
import com.kodilla.cheapflightsearch.view.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends VaadinWebSecurity {
    private final UserDetailsServiceImpl userDetailsService;
    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs/**",
            "/swagger-ui/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/**",
            "/swagger-ui.html",
            "/webjars/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll();
        http.authorizeRequests()
                .antMatchers(
                        "/v1/cheapflightsearch/users/**",
                        "/v1/cheapflightsearch/users",
                        "/v1/cheapflightsearch/airports/update/**"
                ).hasRole("ADMIN");
        http.authorizeRequests()
                .antMatchers(
                        "/v1/cheapflightsearch/itineraries",
                        "/v1/cheapflightsearch/itineraries/**",
                        "/v1/cheapflightsearch/routes",
                        "/v1/cheapflightsearch/routes/**"
                ).authenticated();
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
