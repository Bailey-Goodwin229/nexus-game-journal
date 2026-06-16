package com.goodwin.nexusgamingapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// sets up information for the User table
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    // Creates a private long called id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Unique username that cannot be null
    @Column(unique = true, nullable = false)
    private String username;

    // Password that cannot be null
    @Column(nullable = false)
    private String password;

    // Creates array for entries
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<JournalEntry> entries = new ArrayList<>();

}
