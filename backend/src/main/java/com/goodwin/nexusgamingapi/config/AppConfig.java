package com.goodwin.nexusgamingapi.config;

import com.goodwin.nexusgamingapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.client.RestTemplate;

// class to inject rest template into project instead of hard coding and other security methods for jwt

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    // Tells Spring to create an instance of the RestTemplate tool and keep it in the tool box so I can use it anywhere in the app
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    } // Uses RestTemplate for Twitch/IGDB API call

    // Tells Spring boot who is allowed to log in before we move to the database
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .authorities("USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    }

    // De-coder for passwords, tells Spring Security how to compare passwords from users to the ones in Environmental Variables
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();// Get single Instance of plain text
    }

    // Decision maker who actually checks the ID
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Takes login request and determines if it's valid
    }
}
