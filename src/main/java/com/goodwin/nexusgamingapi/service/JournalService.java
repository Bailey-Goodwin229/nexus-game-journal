package com.goodwin.nexusgamingapi.service;

import com.goodwin.nexusgamingapi.dto.JournalRequestDTO;
import com.goodwin.nexusgamingapi.entity.Game;
import com.goodwin.nexusgamingapi.entity.JournalEntry;
import com.goodwin.nexusgamingapi.repository.GameRepository;
import com.goodwin.nexusgamingapi.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class JournalService {

    // Call repositories
    private final JournalEntryRepository journalRepository;
    private final GameRepository gameRepository;

    public void createEntry(JournalRequestDTO request){

        // Finds the game
        Game game = gameRepository.findByTwitchId(request.twitchId())
                .orElseThrow(() -> new RuntimeException("Could not find game with Twitch ID: " + request.twitchId()));

        // Creates the "Paper" for the journal
        JournalEntry entry = new JournalEntry();

        // Sets the information from the entry
        entry.setRatings(request.rating());
        entry.setNotes(request.notes());
        entry.setEntryDate(LocalDate.now());

        // Tell the entry which game it belongs to
        entry.setGame(game);

        // Save, tells repository to put it into the database
        journalRepository.save(entry);

        System.out.println("SUCCESS: Saved journal entry for " + game.getTitle());
    }
}

