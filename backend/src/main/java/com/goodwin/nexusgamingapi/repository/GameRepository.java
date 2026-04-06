package com.goodwin.nexusgamingapi.repository;

import com.goodwin.nexusgamingapi.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Used to write SQL queries in Game entity
public interface GameRepository extends JpaRepository<Game, Long> {

    // Spring will write the SQL for me automatically using twitchId
    Optional<Game> findByTwitchId(Long twitchId);
    Optional<Game> findByTitle(String title);

    // Adds variable that returns boolean to see if twitchId is saved in two separate places
    boolean existsByTwitchId(Long twitchId);
    boolean existsByTitle(String name);

}
