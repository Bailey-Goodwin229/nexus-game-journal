package com.goodwin.nexusgamingapi.dto;

public record JournalResponseDTO(
        Long journalId,
        String title, // title of the entry
        String gameTitle, // title of the specific game
        String coverArtUrl,
        Integer ratings,
        String notes
) {

}
