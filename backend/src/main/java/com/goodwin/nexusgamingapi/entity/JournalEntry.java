package com.goodwin.nexusgamingapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Entity class for the journal entry information in the database
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {

    // variables for the entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long journalId;

    private int ratings;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDate entryDate;

    // Connects entry to a specific game
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;


}
