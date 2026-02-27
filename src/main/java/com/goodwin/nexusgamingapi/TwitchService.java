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
    @Value("${TWITCH_URL}")
    private String url;
    @Value("${twitch.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public TwitchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String cachedToken;

    public TwitchTokenResponse getAccessToken() {

        // Checks to see if the token is already cached, if it is, return it
        if (this.cachedToken != null) {
            TwitchTokenResponse cachedResponse = new TwitchTokenResponse();
            cachedResponse.setAccessToken(this.cachedToken);
            return cachedResponse;
        }

        // create multi value map to hold multiple values as one object
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "client_credentials");

        // Create headers for twitch to read information appropriately
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        System.out.println("Calling Twitch API...");

        // Fetch the token
        TwitchTokenResponse response = this.restTemplate.postForObject(url, request, TwitchTokenResponse.class);

        // save it to cache before returning
        if (response != null){
            this.cachedToken = response.getAccessToken();
        }

        return response;
    }
}