package com.goodwin.nexusgamingapi.controller;

import com.goodwin.nexusgamingapi.dto.AuthResponseDTO;
import com.goodwin.nexusgamingapi.dto.LoginRequestDTO;
import com.goodwin.nexusgamingapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; // Security boss

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
