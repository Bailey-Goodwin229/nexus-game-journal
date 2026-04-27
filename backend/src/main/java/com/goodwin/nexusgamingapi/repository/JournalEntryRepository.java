package com.goodwin.nexusgamingapi.repository;

import com.goodwin.nexusgamingapi.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Used to write SQL queries for the JournalEntry entity (Database)
@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    // Returns items by the latest item added
    List<JournalEntry> findAllByOrderByCreatedAtDesc();

    // This tells Spring:
    // 1. Find entries
    // 2. Look inside the 'user' object for the 'username'
    // 3. Sort by the 'createdAt' field in descending order
    List<JournalEntry> findByUser_UsernameOrderByCreatedAtDesc(String username);

    List<JournalEntry> findByUser_UsernameAndGame_TitleIgnoreCase(String username, String gameTitle);

    // Finds entries for a specific user where the game title contains the search string
    List<JournalEntry> findByUser_UsernameAndGame_TitleContainingIgnoreCase(String username, String title);
}
