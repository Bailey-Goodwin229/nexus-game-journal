package com.goodwin.nexusgamingapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// Entity class for the journal entry information in the database
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {

    // variables for the entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Creates journal ID
    private long journalId;

    // Creates a ratings integer
    private int ratings;

    // Creates a non-null with message if it is string called title
    @Column(nullable = false)
    @NotBlank(message = "An entry must have a title. Don't leave the page blank!")
    private String title;

    // Creates a non-null, 2000 word, text document that displays a message if blank called notes
    @NotBlank(message = "Write down at least one memory.")
    @Size(max = 2000, message = "The page is full! Try to keep your thoughts under 2000 characters.")
    @Column(columnDefinition = "TEXT")
    private String notes;

    // Time stamp for when an entry is created
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Connects entry to a specific game
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    // Connects the user table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
