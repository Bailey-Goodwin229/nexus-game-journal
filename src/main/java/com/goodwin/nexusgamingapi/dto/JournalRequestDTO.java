package com.goodwin.nexusgamingapi.dto;

import jakarta.validation.constraints.*;

// DTO receives and validates data from user, contract user must "sign" before the database will look at their request
public record JournalRequestDTO(
        // Says this value cannot be null and Twitch ID is required
        @NotNull(message = "Twitch ID is required")
        Long twitchId,

        // Tells us we need a game title
        @NotBlank(message = "Title cannot be empty")
        @Size(max = 100)
        String title,

        // has a ratting between 1 - 10
        @Min(1) @Max(10)
        int ratings,

        // Notes can be no longer than 1000 characters
        @Size(max = 1000)
        String notes
) {}
