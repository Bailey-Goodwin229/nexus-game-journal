package com.goodwin.nexusgamingapi.dto;

public record JournalRequestDTO(
        Long twitchId,
        String title,
        int ratings,
        String notes
) {}
