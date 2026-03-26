package com.goodwin.nexusgamingapi.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            // 1. disable CSRF (Standard for stateless REST APIs)
            .csrf(csrf -> csrf.disable())

            // 2. Define the "Rules of the road"
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/journal/search/**").permitAll() // Public: Anyone can search
                .anyRequest().authenticated()                          // Private: Everything else needs a login
        )

            // 3. Enable Basic Auth (The "Fast Pass")
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
