package com.goodwin.nexusgamingapi.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

// Takes any kind of "crash" or error and translates it to be returned to the user with a message
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catch when DTO Validation rules are broken
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>(); // Stores mistakes in dictionary

        // Loop through all the "Rules" that were broken
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors); // sends an error response back
    }

    // Catch "Wrong Password/Username" errors
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Incorrect username or password!");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // Catch duplicate username errors (SQL unique constraint violation)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUser(DataIntegrityViolationException ex) {
        Map<String, String> error = new HashMap<>();

        // This is a broad exception, but in a registration context,
        // it almost always means the username is taken.
        error.put("error", "That alias is already in the archive. Please choose another.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // Global "Catch-All" for unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "The Nexus is flickering. An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // Global check for game page that doesn't exist
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EntityNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "The archives are incomplete! This entry seems to have been erased.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); // 404 Not Found
    }

    // Catch when a user tries to touch an entry that doesn't belong to them
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "You don't have permission to edit this person's journal!");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // Catch when trying to update/delete a non-existent ID
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Map<String, String>> handleEmptyResult(EmptyResultDataAccessException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "The entry you're looking for has already been erased.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
