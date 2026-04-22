package com.goodwin.nexusgamingapi.controller;

import com.goodwin.nexusgamingapi.dto.JournalRequestDTO;
import com.goodwin.nexusgamingapi.dto.JournalResponseDTO;
import com.goodwin.nexusgamingapi.service.JournalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller to direct data for the journal service
@RestController
@RequestMapping("/api/journal")
@RequiredArgsConstructor
public class JournalController {

    // Calls the journal service class into this class
    private final JournalService journalService;

    // sets route for post request (Create)
    @PostMapping("/save")
    public ResponseEntity<JournalResponseDTO> saveReview(@Valid @RequestBody JournalRequestDTO request){
        // We catch the returned DTO from the service
        JournalResponseDTO savedEntry = journalService.createEntry(request);

        // Wrap it in a ResponseEntity with a 201 Created status
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    // Read
    @GetMapping
    public ResponseEntity<List<JournalResponseDTO>> getAllEntries(){
        List<JournalResponseDTO> entries = journalService.getAllJournalEntries();
        return ResponseEntity.ok(entries); // Shorthand for 200 OK
    }

    @GetMapping("game/{gameTitle}")
    public ResponseEntity<List<JournalResponseDTO>> getEntriesByGame(@PathVariable String gameTitle) {
        return ResponseEntity.ok(journalService.getEntriesByGameTitle(gameTitle));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<JournalResponseDTO> update(@PathVariable Long id, @Valid @RequestBody JournalRequestDTO request) {
        // Pass the ID from url and data from the body to the service
        JournalResponseDTO updated = journalService.updateEntry(id, request);
        return ResponseEntity.ok(updated); // return updated message with response
    }

    // Destroy
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        journalService.deleteEntry(id); // deletes entry if found using method in journal service
        return ResponseEntity.noContent().build(); // return response with no content when done
    }

    // Allows user to search the database for journal return and returns it to the screen if found
    @GetMapping("/search")
    public ResponseEntity<List<JournalResponseDTO>> searchByTitle(@RequestParam String title){
        List<JournalResponseDTO> results = journalService.searchByGameTitle(title); // gets a list of results from journal service
        return ResponseEntity.ok(results); // returns result with response
    }
}