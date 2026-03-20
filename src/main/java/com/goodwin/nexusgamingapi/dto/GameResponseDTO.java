package com.goodwin.nexusgamingapi.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GameResponseDTO(Long id, String title, String gameName, String summary, Cover cover){

    // Nested record to handle the cover image object
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Cover(String url){}

}
