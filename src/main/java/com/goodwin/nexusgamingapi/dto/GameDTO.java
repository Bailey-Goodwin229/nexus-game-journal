package com.goodwin.nexusgamingapi.dto;

// Record that will only display this information that it gets from the game instead of ALL the information the game comes with from the API
public record GameDTO(Long id, String title, String summary, String coverArtUrl) {}
