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

    }

}
