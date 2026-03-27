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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${APP_USERNAME}")
    private String appUsername;

    @Value("${APP_PASSWORD}")
    private String appPassword;

    private final GlobalExceptionHandler globalExceptionHandler;

    // Used to create an account to navigate around the application with approval
    @Bean
    public UserDetailsService userDetailsService(){
        // This creates a bailey user in memory so the system can find it
        UserDetails user = User.withUsername(appUsername)
                .password(appPassword)
                .authorities("USER")
                .build();

        return new InMemoryUserDetailsManager(user);

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // Tells spring to compare passwords as plain text for now
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. disable CSRF (Standard for stateless REST APIs)
                .csrf(csrf -> csrf.disable())

                // 2. Define the "Rules of the road"
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Makes login station public
                        .requestMatchers("/api/journal/search/**").permitAll() // Public: Anyone can search
                        .anyRequest().authenticated()                          // Private: Everything else needs a login
                )

                // 3. Enable Basic Auth (The "Fast Pass")
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}


