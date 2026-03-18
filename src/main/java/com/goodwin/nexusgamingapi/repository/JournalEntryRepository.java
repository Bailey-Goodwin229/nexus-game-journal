package com.goodwin.nexusgamingapi.repository;

import com.goodwin.nexusgamingapi.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {


}
