package com.goodwin.nexusgamingapi;
// Class to use the keys so I can make an api call

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TwitchService {

    @Value("${twitch.client.id}")
    private String clientId;

    @Value("${twitch.client.secret}")
    private String clientSecret;


}
