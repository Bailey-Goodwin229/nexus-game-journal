package com.goodwin.nexusgamingapi.dto;

// DTO for authorization response to put it in JSON format for front-end
public record AuthResponseDTO(String token, String username, String password) {}
