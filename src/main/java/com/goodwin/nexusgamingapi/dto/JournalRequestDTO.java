package com.goodwin.nexusgamingapi.dto;

public record JournalRequestDTO(
        String twitchId,
        int rating,
        String notes
) {}
