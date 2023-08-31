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
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService users() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{noop}0001a")
//                .roles(String.valueOf(UserRole.USER))
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{noop}0002b")
//                .roles(String.valueOf(UserRole.USER), String.valueOf(UserRole.ADMIN))
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}
