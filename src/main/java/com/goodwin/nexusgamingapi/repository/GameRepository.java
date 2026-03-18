package com.goodwin.nexusgamingapi.repository;

import com.goodwin.nexusgamingapi.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    // Spring will write the SQL for me automatically using twitchId
    Optional<Game> findByTwitchId(String twitchId);

    // Adds variable that returns boolean to see if twitchId is saved in two separate places
    boolean existsByTwitchId(String twitchId);
    boolean existsByTitle(String name);

}
