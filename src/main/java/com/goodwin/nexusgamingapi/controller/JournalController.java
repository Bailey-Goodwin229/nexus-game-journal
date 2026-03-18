package com.goodwin.nexusgamingapi.controller;

import com.goodwin.nexusgamingapi.dto.JournalRequestDTO;
import com.goodwin.nexusgamingapi.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller to direct data for the journal service
@RestController
@RequestMapping("/api/journal")
@RequiredArgsConstructor
public class JournalController {

    // Calls the journal service class into this class
    private final JournalService journalService;

    // sets route for post request
    @PostMapping("/save")
    public void saveReview(@RequestBody JournalRequestDTO request){
        journalService.createEntry(request);
    }
}