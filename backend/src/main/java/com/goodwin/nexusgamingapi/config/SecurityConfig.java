package com.goodwin.nexusgamingapi.config;


import com.goodwin.nexusgamingapi.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final GlobalExceptionHandler globalExceptionHandler; // Catches any errors at the front and will translate it

    // Inject the filter
    private final JwtAuthenticationFilter jwtAuthFilter;

    // "Master BLueprint" for app security! Tells spring which doors to lock, which keys to accept, and how the guard should behave
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Enable CORS using our Bean
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 1. disable CSRF (Standard for stateless REST APIs)
                // Use the method reference to disable CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Define the "Rules of the road"
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Makes login station public
                        .requestMatchers("/api/journal/search/**").permitAll() // Public: Anyone can search
                        .anyRequest().authenticated()                          // Private: Everything else needs a login
                )

                // Switch to Stateless (No more Cookies!)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                //  Put our Guard at the very front of the line
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Squishes all the rules into a security chain for spring to use and enforce.
    }

    // This bean defines the "rules of engagement" for browsers. Tells the backend to trust your React development server and allow the Authorization header.
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. Define the origin: The Vite/React dev server
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // 2. Define the methods: Allow all the standard CRUD operations
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. Define the headers: Crucial to allow authorization for our JWT
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // 4. Allows for credentials if I wanted to use cookies
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}


