package com.goodwin.nexusgamingapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// Holds the game data
@NoArgsConstructor
@Entity
@Table(name = "games") // Names table games
@Data
public class Game {

    // Creates unique ID variable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Creates a column for title and doesn't allow the title to be null
    @Column(nullable = false)
    private String title;

    // declares a String to hold the summary, Lob ensures it can hold a lot of information
    @Lob
    private String summary;

    // Creates a unique column for twitchId
    @Column(unique = true)
    private Long twitchId;

    // Variables for box art and igdb id
    private String coverArtUrl;
    private String igdbId;

    // Creates time stamp variables for when an entry is created or updated
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public void setURL() {
    }
}
