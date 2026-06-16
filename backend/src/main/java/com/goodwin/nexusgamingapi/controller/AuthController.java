package com.goodwin.nexusgamingapi.controller;

import com.goodwin.nexusgamingapi.dto.AuthResponseDTO;
import com.goodwin.nexusgamingapi.dto.LoginRequestDTO;
import com.goodwin.nexusgamingapi.entity.User;
import com.goodwin.nexusgamingapi.repository.UserRepository;
import com.goodwin.nexusgamingapi.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Injects other functions or layouts we need
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Sets up endpoint for the register functionality
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user, HttpServletResponse response) {
        // 1. Check if username is already taken
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity
                    .status(400)
                    .body(Map.of("error", "Username is already taken. Please choose another one."));
        }

        // 2. Proceed with registration if unique
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // Creates unique username and token
        String token = jwtService.generateToken(user.getUsername());
        ResponseCookie cookie = createTokenCookie(token, 24 * 60 * 60); // 24 hours

        // Returns cookie and allows access
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("User registered and logged in: " + user.getUsername());
    }

    // Endpoint for login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        // Delegates the username and password to Spring Security; this throws an exception automatically if credentials do not match.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // Generates new token and cookie for accepted value
        String token = jwtService.generateToken(request.username());
        ResponseCookie cookie = createTokenCookie(token, 24 * 60 * 60); // 24 hours

        // Allows user in with 200 and cookie
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Authenticated: " + request.username());
    }

    // User profile endpoint
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(org.springframework.security.core.Authentication authentication) {
        // Verifies if an authentication context exists and if the user is successfully validated.
        if (authentication == null || !authentication.isAuthenticated()) {
            // If not it denies access with 401 error
            return ResponseEntity.status(401).body("Not authenticated");
        }
        // Else it allows the user in
        return ResponseEntity.ok(authentication.getName());
    }

    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Pass 0 to completely purge the cookie using the exact same configuration blueprint
        ResponseCookie cookie = createTokenCookie("", 0);

        // set to expired cookie which send te user back to the login page
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }

    // Single-source of truth for cookie construction
    private ResponseCookie createTokenCookie(String token, long maxAgeInSeconds) {
        return ResponseCookie.from("nexus_token", token)
                .httpOnly(true)
                .secure(true) // Set to true in Production with HTTPS
                .path("/")
                .maxAge(maxAgeInSeconds)
                .sameSite("None") // Enforced for matching registration, login, and logout handshakes
                .build();
    }
}
