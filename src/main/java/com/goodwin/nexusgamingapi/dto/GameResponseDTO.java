package com.goodwin.nexusgamingapi.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Record that catches information from the API and holds it in the appropriate format
// All the other DTOs are used for the front-end but this own is used externally as an "API Translator"
@JsonIgnoreProperties(ignoreUnknown = true)
public record GameResponseDTO(Long id,
                              @JsonProperty("name")
                              String gameName,
                              String summary,
                              Cover cover){

    // Nested record to handle the cover image object
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Cover(String url){}

}
