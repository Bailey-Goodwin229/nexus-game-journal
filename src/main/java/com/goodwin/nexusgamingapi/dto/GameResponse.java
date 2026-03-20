package com.goodwin.nexusgamingapi.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GameResponse (Long id, String name, String summary, Cover cover){

    // Nested record to handle the cover image object
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Cover(String url){}

}
