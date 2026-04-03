package com.goodwin.nexusgamingapi.dto;

import java.time.LocalDateTime;

// Sends the following information back to the user to see when used
public record JournalResponseDTO(
        Long journalId,
        String title, // title of the entry
        String gameTitle, // title of the specific game
        String coverArtUrl,
        Integer ratings,
        String notes,
        LocalDateTime createdAt
) {}
