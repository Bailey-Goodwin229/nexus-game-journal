import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api/axios'; // Imports information from axios
import AddEntryForm from './AddEntryForm';

/*
This is your Detail View.
Its job is to act like a specific "folder" in your archive, showing only the notes for the game you clicked on.
 */

const GameJournalPage = () => {

    const { gameTitle } = useParams(); // Grabs the title from the URl
    const [entries, setEntries] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchGameEntries = async () => {
            try {
                // You can either fetch all and filter, or hit a specific endpoint.
                // Even though you're on a new page, it still calls the same backend endpoint to get the list of entries.
                const response = await api.get('/journal');
                //This is the logic that turns the "Giant List" into a "Specific List." It compares every entry's gameTitle to the one in the URL.
                const filtered = response.data.filter(e => {
                    const titleFromEntry = e.gameTitle || e.game?.title;
                    return titleFromEntry === decodeURIComponent(gameTitle);
                });

                setEntries(filtered);
                setLoading(false);
            } catch (err) {
                // Or catches and displays error message.
                console.error("Failed to load entries", err);
                setLoading(false);
            }
        };
        fetchGameEntries();
        // The dependency array ensures that if you somehow navigate from one game detail page directly to another, the page will refresh with the new game's data.
    }, [gameTitle]);

    if (loading) return <div>Loading the Archive...</div>;

    return (
        <div className="game-detail-page" style={{ padding: '20px' }}>
            {/* A button that sends the user back to the main list. Because it’s a Link, it doesn’t reload the whole site; it just swaps the view back instantly. */}
            <Link to='/journal' style={{ color: '#00cf5d' }}>← Back to Archive</Link>
            {/* Displays the name of the game at the top of the page. */}
            <h1>{decodeURIComponent(gameTitle)}</h1>
            <AddEntryForm
                preselectedGame={decodeURIComponent(gameTitle)}
                onEntryAdded={(newEntry) => setEntries(prevEntries => [newEntry, ...prevEntries])}
            />
            <div className="entries-grid">
                {/* This loop draws a card for every journal entry found for this specific game. */}
                {entries.map(entry => (
                    <div key={entry.journalId} className="game-card">
                        <h3>{entry.title}</h3>
                        <p>{entry.notes}</p>
                        {/* This is your Star Rating display logic.
                        It creates 10 spans and colors them gold (#ffc107) if the index is less than the entry's rating, and gray (#444) if it's higher. */}
                        <div className="rating">
                            Rating: {[...Array(10)].map((_, index) => (
                            <span key={index} style={{ color: index < entry.ratings ? '#ffc107' : '#444' }}>★</span>
                        ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default GameJournalPage;