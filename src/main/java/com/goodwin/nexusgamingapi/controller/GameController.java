package com.goodwin.nexusgamingapi.controller;

import com.goodwin.nexusgamingapi.TwitchService;
import com.goodwin.nexusgamingapi.dto.GameResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final TwitchService twitchService;

    // constructor injects TwitchService
    public GameController(TwitchService twitchService){
        this.twitchService = twitchService;
    }

    @GetMapping("/search")
    public List<GameResponse> searchGames(@RequestParam String gameName){
        return twitchService.searchGame(gameName);
    }

}
