package com.goodwin.nexusgamingapi.service;

import com.goodwin.nexusgamingapi.dto.JournalRequestDTO;
import com.goodwin.nexusgamingapi.dto.JournalResponseDTO;
import com.goodwin.nexusgamingapi.entity.Game;
import com.goodwin.nexusgamingapi.entity.JournalEntry;
import com.goodwin.nexusgamingapi.repository.GameRepository;
import com.goodwin.nexusgamingapi.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Journal Service does the heavy lifting and houses the methods for the journal Controller
@Service
@RequiredArgsConstructor
public class JournalService {

    // Call repositories
    private final JournalEntryRepository journalEntryRepository;
    private final GameRepository gameRepository;

    public JournalResponseDTO createEntry(JournalRequestDTO request){

        // Finds the game
        Game game = gameRepository.findByTitle(request.gameTitle())
                .orElseThrow(() -> new RuntimeException("Could not find game with Twitch ID: " + request.gameTitle()));

        // Creates the "Paper" for the journal
        JournalEntry entry = new JournalEntry();

        // Sets the information from the entry
        entry.setTitle(request.title());
        entry.setRatings(request.ratings());
        entry.setNotes(request.notes());
        entry.setCreatedAt(LocalDateTime.now());

        // Tell the entry which game it belongs to
        entry.setGame(game);


        // Save, tells repository to put it into the database
        JournalEntry savedEntry = journalEntryRepository.save(entry);

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
                entry.getNotes(),
                entry.getCreatedAt()
        );
    }

    // Method that fetches entries from database and turns them into a list
    public List<JournalResponseDTO> getAllJournalEntries(){
        return journalEntryRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();

    }

    // Method to update journal entry
    public JournalResponseDTO updateEntry(Long journalId, JournalRequestDTO request){

        // Finds the journal id
        JournalEntry entry = journalEntryRepository.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal entry not found with ID: " + journalId));

        // Finds the game
        Game game = gameRepository.findByTitle(request.gameTitle())
                .orElseThrow(() -> new RuntimeException("Could not find game with Twitch ID: " + request.gameTitle()));


        // Sets the information from the entry
        entry.setTitle(request.title());
        entry.setRatings(request.ratings());
        entry.setNotes(request.notes());
        entry.setCreatedAt(LocalDateTime.now());

        // Tell the entry which game it belongs to
        entry.setGame(game);


        // Save, tells repository to put it into the database
        JournalEntry updatedEntry = journalEntryRepository.save(entry);

        System.out.println("SUCCESS: Updated journal entry ID " + journalId);

        return mapToResponseDTO(updatedEntry);
    }

    // Method to delete an entry
    public void deleteEntry(Long id){
        // Check if entry exists
        if (!journalEntryRepository.existsById(id)){
            throw new RuntimeException("Cannot delete: Entry " + id + " does not exist.");
        }
        journalEntryRepository.deleteById(id);
        System.out.println("SUCCESS: Deleted journal entry ID " + id);
    }

    // Returns a list of games using JournalRepository that user searches, shows them results.
    public List<JournalResponseDTO> searchByGameTitle(String title){
        return journalEntryRepository.findByGame_TitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
}

