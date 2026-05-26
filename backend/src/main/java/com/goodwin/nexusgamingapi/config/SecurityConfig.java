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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. Allow the error path so we can see real error messages
                        .requestMatchers("/error/**").permitAll()
                        // 2. Allow pre-flight OPTIONS requests (crucial for 'withCredentials')
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/journal/search/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // This bean defines the "rules of engagement" for browsers. Tells the backend to trust your React development server and allow the Authorization header.
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. Define the origin: The Vite/React dev server
        // 1. ADD YOUR VERCEL URL HERE (Keep localhost so you can still test locally!)
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "nexus-game-journal.vercel.app",
                "https://*.vercel.app",                                // Allows all Vercel preview deploys
                "https://nexus-game-journal-*-bailey-goodwin-s-projects.vercel.app" // Tailored exact match fallback
        ));

        // 2. Define the methods: Allow all the standard CRUD operations
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. Define the headers: Crucial to allow authorization for our JWT
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // 4. Allows for credentials if I wanted to use cookies
        configuration.setAllowCredentials(true);

        // 5. Allows the frontend to receive the Set-Cookie header
        configuration.setExposedHeaders(List.of("Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}


