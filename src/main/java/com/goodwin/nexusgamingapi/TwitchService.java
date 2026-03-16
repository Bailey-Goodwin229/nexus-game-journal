package com.goodwin.nexusgamingapi;


import com.goodwin.nexusgamingapi.dto.GameResponse;
import com.goodwin.nexusgamingapi.entity.Game;
import com.goodwin.nexusgamingapi.repository.GameRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Service
public class TwitchService {

    @Value("${twitch.client.id}")
    private String clientId;
    @Value("${twitch.url}")
    private String twitchUrl;
    @Value("${twitch.client.secret}")
    private String clientSecret;
    @Value("${twitch.api.url}")
    private String twitchApiUrl;

    private String cachedToken;
    private LocalDateTime expirationTime;

    private final GameRepository gameRepository;

    private final RestTemplate restTemplate;

    public TwitchService(GameRepository gameRepository, RestTemplate restTemplate) {
        this.gameRepository = gameRepository;
        this.restTemplate = restTemplate;
    }

    public TwitchTokenResponse getAccessToken() {

        // Checks to see if the token is already cached, if it is, return it
        if (this.cachedToken != null && expirationTime != null && LocalDateTime.now().isBefore(expirationTime)) {
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
        TwitchTokenResponse response = this.restTemplate.postForObject(twitchUrl, request, TwitchTokenResponse.class);

        // save it to cache before returning
        if (response != null){
            this.cachedToken = response.getAccessToken();
            this.expirationTime = LocalDateTime.now().plusSeconds(response.getExpiresIn());
        }

        return response;
    }

    public List<GameResponse> searchGame(String gameName){
        // Get the token from verified cache
        String token = getAccessToken().getAccessToken();

        // initialize headers
        HttpHeaders headers = new HttpHeaders();

        // Set client ID
        headers.set("Client-ID", clientId);

        // Set the authorization
        headers.set("Authorization", "Bearer " + token);

        // Build the Query Body
        String body = String.format("fields name, summary, cover.url; search \"%s\"; limit 10;", gameName);

        // Wrap Headers and Body into an HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        // Make the POST call
        // IGDB returns an ARRAY of games, so we map it to GameResponse[]
        GameResponse[] response = restTemplate.postForObject(
                twitchApiUrl,
                requestEntity,
                GameResponse[].class
        );

        // Create a set to ensure only unique games are in the list from ID's we've seen form this search
        Set<String> seenIds = new HashSet<>();

        // Uses for loop to go through games and save them to database and filters stream using the new set
        if (response != null){
            Arrays.stream(response)
                    .filter(game -> seenIds.add(game.name()))
                    .forEach(this::saveGameFromTwitch);
        }

        // Convert Array to list
        return response != null ? Arrays.asList(response) : Collections.emptyList();
    }

    // Function to save game data into database
    public void saveGameFromTwitch(GameResponse response){


        // Checks to make sure there isn't already a saved game before saving
        if (!gameRepository.existsByTitle(response.name())){

            // Initialize new empty "Database Raw"
            Game gameEntity = new Game();

            gameEntity.setTwitchId(response.id());
            gameEntity.setTitle(response.name());

            gameRepository.save(gameEntity);
            System.out.println("Saving new game: " + response.name() + " [ID: " + response.id() + "]");

        }

    }

}