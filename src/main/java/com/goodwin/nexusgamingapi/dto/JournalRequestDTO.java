package com.goodwin.nexusgamingapi.dto;

import jakarta.validation.constraints.*;

public record JournalRequestDTO(
        @NotNull(message = "Twitch ID is required")
        Long twitchId,

        @NotBlank(message = "Title cannot be empty")
        @Size(max = 100)
        String title,

        @Min(1) @Max(10)
        int ratings,

        @Size(max = 1000)
        String notes
) {}
