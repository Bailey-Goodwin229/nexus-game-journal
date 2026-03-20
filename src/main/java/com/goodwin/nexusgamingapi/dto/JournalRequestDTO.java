package com.goodwin.nexusgamingapi.dto;

public record JournalRequestDTO(
        Long twitchId,
        int rating,
        String notes
) {}
