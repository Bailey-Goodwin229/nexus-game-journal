package com.goodwin.nexusgamingapi;
// Class to use the keys so I can make an api call

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TwitchService {

    @Value("${twitch.client.id}")
    private String clientId;

    @Value("${twitch.client.secret}")
    private String clientSecret;

    private String url = "https://id.twitch.tv/oauth2/token";

    public TwitchTokenResponse getAccessToken(){

        // create multi value map to hold multiple values as one object
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "client_credentials");

        // Create headers for twitch to read information appropriately
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(
                url,
                request,
                TwitchTokenResponse.class);

        return restTemplate.postForObject(url, request, TwitchTokenResponse.class);
    }
}


