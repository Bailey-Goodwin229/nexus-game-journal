package com.goodwin.nexusgamingapi;

import com.goodwin.nexusgamingapi.service.JwtService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NexusGamingApiApplication {

    // Main function just acts as a way to "run" the server while the other applications (controllers) direct what actions are performed
    // API handshake already happens in other methods, I don't need to call it here
    // "Lazy Loading" ~ you don't do the work until someone asks for it.
    public static void main(String[] args) {
        var context = SpringApplication.run(NexusGamingApiApplication.class, args);

    }

}
