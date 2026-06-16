package com.goodwin.nexusgamingapi.repository;

import com.goodwin.nexusgamingapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Declares a public Java interface named UserRepository
//  Inherits from Spring Data's JpaRepository. This automatically gives the interface standard database methods like save(), findById(), findAll(), and deleteById()
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring parses the method name (findBy + Username) and automatically generates the SQL query: SELECT * FROM users WHERE username = ?.
    // Wraps the result to safely handle cases where the username might not exist, preventing NullPointerException errors in your controller.
    Optional<User> findByUsername(String username);
}
