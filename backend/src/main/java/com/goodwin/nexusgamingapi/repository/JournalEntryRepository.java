package com.goodwin.nexusgamingapi.repository;

import com.goodwin.nexusgamingapi.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Used to write SQL queries for the JournalEntry entity (Database)
@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    // Returns items by the latest item added
    List<JournalEntry> findAllByOrderByCreatedAtDesc();

    // Query
    // 1. FindBy -> standard JPA start
    // 2. Game -> looks at game entity inside journal
    // 3. _Title -> Looks at the title field inside the game
    // 4. ContainingIgnoreCase -> Make it a fuzzy, case-insensitive search
    List<JournalEntry> findByGame_TitleContainingIgnoreCase(String title);



}
