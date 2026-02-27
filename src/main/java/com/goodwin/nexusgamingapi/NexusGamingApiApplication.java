package com.goodwin.nexusgamingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NexusGamingApiApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(NexusGamingApiApplication.class, args);

        // Gets the twitch service we just made
        TwitchService service = context.getBean(TwitchService.class);

        // calls new method and prints result for handshake to get access to api
        TwitchTokenResponse response = service.getAccessToken();

        if (response != null){
            System.out.println("--- SUCCESS! ---");
            System.out.println("Access Token: " + response.getAccessToken());
            System.out.println("Expires In: " + response.getExpiresIn());
            System.out.println("Token Type: " + response.getTokenType());
            System.out.println("Access Token: " + response.getAccessToken());
        }

    }

}
