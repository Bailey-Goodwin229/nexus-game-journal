package com.goodwin.nexusgamingapi.repository;

import com.goodwin.nexusgamingapi.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    // Returns items by the latest item added
    List<JournalEntry> findAllByOrderByEntryDateDesc();

}
