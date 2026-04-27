package com.goodwin.nexusgamingapi.service;

import com.goodwin.nexusgamingapi.dto.JournalRequestDTO;
import com.goodwin.nexusgamingapi.dto.JournalResponseDTO;
import com.goodwin.nexusgamingapi.entity.Game;
import com.goodwin.nexusgamingapi.entity.JournalEntry;
import com.goodwin.nexusgamingapi.entity.User;
import com.goodwin.nexusgamingapi.repository.GameRepository;
import com.goodwin.nexusgamingapi.repository.JournalEntryRepository;
import com.goodwin.nexusgamingapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    public JournalResponseDTO createEntry(JournalRequestDTO request){

        // Finds the game
        Game game = gameRepository.findByTitle(request.gameTitle())
                .orElseThrow(() -> new RuntimeException("Could not find game with Twitch ID: " + request.gameTitle()));

        // Grab the logged-in user's name from Spring Security
        String currentUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        // Find the User object in your database
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found!" + currentUsername));

        // Creates the "Paper" for the journal
        JournalEntry entry = new JournalEntry();

        // Sets the information from the entry
        entry.setTitle(request.title());
        entry.setRatings(request.ratings());
        entry.setNotes(request.notes());
        entry.setCreatedAt(LocalDateTime.now());

        // Tell the entry which game it belongs to
        entry.setGame(game);

        // LINK THE USER HERE
        entry.setUser(currentUser);

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
        String currentUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        // You'll need to add findByUser_UsernameOrderByCreatedAtDesc to your Repository!
        return journalEntryRepository.findByUser_UsernameOrderByCreatedAtDesc(currentUsername)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // Method that fetches a specific entry and turns it into a list
    public List<JournalResponseDTO> getEntriesByGameTitle(String gameTitle) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // You'll need to add this method to your Repo too!
        return journalEntryRepository.findByUser_UsernameAndGame_TitleIgnoreCase(currentUsername, gameTitle)
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

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        entry.setUser(currentUser);

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
        // 1. Get the current logged-in user
        String currentUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        // 2. Search ONLY this user's entries
        return journalEntryRepository.findByUser_UsernameAndGame_TitleContainingIgnoreCase(currentUsername, title)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
}

