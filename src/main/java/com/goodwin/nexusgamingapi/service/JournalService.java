package com.goodwin.nexusgamingapi.service;

import com.goodwin.nexusgamingapi.dto.JournalRequestDTO;
import com.goodwin.nexusgamingapi.dto.JournalResponseDTO;
import com.goodwin.nexusgamingapi.entity.Game;
import com.goodwin.nexusgamingapi.entity.JournalEntry;
import com.goodwin.nexusgamingapi.repository.GameRepository;
import com.goodwin.nexusgamingapi.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalService {

    // Call repositories
    private final JournalEntryRepository journalRepository;
    private final GameRepository gameRepository;

    public JournalResponseDTO createEntry(JournalRequestDTO request){

        // Finds the game
        Game game = gameRepository.findByTwitchId(request.twitchId())
                .orElseThrow(() -> new RuntimeException("Could not find game with Twitch ID: " + request.twitchId()));

        // Creates the "Paper" for the journal
        JournalEntry entry = new JournalEntry();

        // Sets the information from the entry
        entry.setTitle(request.title());
        entry.setRatings(request.ratings());
        entry.setNotes(request.notes());
        entry.setEntryDate(LocalDate.now());

        // Tell the entry which game it belongs to
        entry.setGame(game);


        // Save, tells repository to put it into the database
        JournalEntry savedEntry = journalRepository.save(entry);

        System.out.println("SUCCESS: Saved journal entry for " + game.getTitle());

        return mapToResponseDTO(savedEntry);
    }

    // Helper method for mapping DTO
    private JournalResponseDTO mapToResponseDTO(JournalEntry entry){
        return new JournalResponseDTO(
                entry.getJournalId(),
                entry.getTitle(),
                entry.getGame().getTitle(),
                entry.getGame().getCoverArtUrl(),
                entry.getRatings(),
                entry.getNotes()
        );
    }

    // Method that fetches entries from database and turns them into a list
    public List<JournalResponseDTO> getAllJournalEntries(){
        return journalRepository.findAllByOrderByEntryDateDesc()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();

    }
}

