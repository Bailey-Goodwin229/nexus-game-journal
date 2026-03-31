package com.goodwin.nexusgamingapi;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data; // Lombok adds getters and setters for me

// class for holding the twitch token response
@Data
public class TwitchTokenResponse {

    // variables
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private int expiresIn;
    @JsonProperty("token_type")
    private String tokenType;

}
