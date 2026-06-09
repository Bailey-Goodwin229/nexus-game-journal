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

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user, HttpServletResponse response) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        ResponseCookie cookie = createTokenCookie(token, 24 * 60 * 60); // 24 hours

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("User registered and logged in: " + user.getUsername());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        String token = jwtService.generateToken(request.username());
        ResponseCookie cookie = createTokenCookie(token, 24 * 60 * 60); // 24 hours

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Authenticated: " + request.username());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(org.springframework.security.core.Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        return ResponseEntity.ok(authentication.getName());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Pass 0 to completely purge the cookie using the exact same configuration blueprint
        ResponseCookie cookie = createTokenCookie("", 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }

    // Senior Strategy: Single-source of truth for cookie construction
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
