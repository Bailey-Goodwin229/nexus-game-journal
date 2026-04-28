package com.goodwin.nexusgamingapi.controller;

import com.goodwin.nexusgamingapi.dto.AuthResponseDTO;
import com.goodwin.nexusgamingapi.dto.LoginRequestDTO;
import com.goodwin.nexusgamingapi.entity.User;
import com.goodwin.nexusgamingapi.repository.UserRepository;
import com.goodwin.nexusgamingapi.service.JwtService;
import lombok.RequiredArgsConstructor;
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
    private final AuthenticationManager authenticationManager; // Security boss
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        // 1. Encode the raw password from the frontend
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 2. Save the user with the hashed password
        userRepository.save(user);

        // 3. Auto-login by returning a token immediately
        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponseDTO(token, user.getUsername()));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request){
        // 1. Ask spring security to verify credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. If we reach the line, login was a success! Mint token.
        String token = jwtService.generateToken(request.username());

        // return token with a response
        return ResponseEntity.ok(new AuthResponseDTO(token, request.username()));
    }
}
