package com.goodwin.nexusgamingapi.controller;

import com.goodwin.nexusgamingapi.dto.JournalRequestDTO;
import com.goodwin.nexusgamingapi.dto.JournalResponseDTO;
import com.goodwin.nexusgamingapi.service.JournalService;
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

    // sets route for post request
    @PostMapping("/save")
    public ResponseEntity<JournalResponseDTO> saveReview(@RequestBody JournalRequestDTO request){
        // We catch the returned DTO from the service
        JournalResponseDTO savedEntry = journalService.createEntry(request);

        // Wrap it in a ResponseEntity with a 201 Created status
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JournalResponseDTO>> getAllEntries(){
        List<JournalResponseDTO> entries = journalService.getAllJournalEntries();
        return ResponseEntity.ok(entries); // Shorthand for 200 OK
    }
}