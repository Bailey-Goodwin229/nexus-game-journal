import React, { useEffect, useState } from 'react';
import api from '../api/axios';
import AddEntryForm from './AddEntryForm';// Our "Senior" Axios instance with the Interceptor

const JournalFeed = () => {
    // 1. State Management
    const [entries, setEntries] = useState([]); // Our list of JournalResponseDTOs
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 2. The API Call (The "Fetcher")
    useEffect(() => {
        const fetchJournal = async () => {
            try {
                // Because of our Axios Interceptor, the JWT is added AUTOMATICALLY here!
                const response = await api.get('/journal');
                setEntries(response.data); // Save te DTO list to state
                setLoading(false);
            } catch (err) {
                setError("Could not load your journal.");
                setLoading(false);
            }
        };

        fetchJournal();
        }, []); // The empty array [] means "Only run this once when the page loads"

    // 3. Conditional Rendering (Handling Loading/Errors)
    if (loading) return <div className="nexus-loader">Scanning The Nexus...</div>;
    if (error) return <div className="nexus-error">{error}</div>;

    return (
        <div className="journal-feed">
            <h1>Your Gaming Archive</h1>
            <AddEntryForm onEntryAdded={(newEntry) => setEntries([newEntry, ...entries])} />
            <div className="entries-grid">
                {/* 4. The "Map" (Like a for-each loop in Java/Thymeleaf) */}
                {entries.length > 0 ? (
                    entries.map((entry) => (
                        <div key={entry.journalId} className="game-card">
                            <h3>{entry.gameTitle}</h3>
                            <h3 className="entry-title">{entry.title}</h3>
                            <p className="rating">Rating: {entry.ratings}/10</p>
                            <p className="notes">{entry.notes}</p>
                            <small>Logged on: {entry.createdAt ? new Date(entry.createdAt).toLocaleDateString() : 'Pending...'}</small>
                        </div>
                    ))
                ) : (
                    <p>No entries found. Time to play some games!</p>
                )}
            </div>
        </div>
    );
};

const handleEntryAdded = (newEntry) => {
    // Take the existing entries and add the new one to the front!
    setEntries([newEntry, ...entries]);
};

export default JournalFeed;