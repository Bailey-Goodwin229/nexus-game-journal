import React, { useEffect, useState } from 'react';
import { useParams, Link, useLocation } from 'react-router-dom';
import api from '../api/axios'; // Imports information from axios
import AddEntryForm from './AddEntryForm';

/*
This is your Detail View.
Its job is to act like a specific "folder" in your archive, showing only the notes for the game you clicked on.
 */

const GameJournalPage = () => {

    const { gameTitle } = useParams(); // Grabs the title from the URl
    const location = useLocation();
    const gameData = location.state || {};
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
                twitchId={gameData.twitchId}
                coverArt={gameData.coverArtUrl}
                onEntryAdded={(newEntry) => setEntries(prevEntries => [newEntry, ...prevEntries])}
            />
            <div className="entries-grid">
                {entries.map(entry => (
                    <div key={entry.journalId} className="game-card" style={{ padding: '30px', marginBottom: '20px', position: 'relative' }}>
                        {/* 1. Centered Title */}
                        <h3 style={{
                            textAlign: 'center',
                            marginBottom: '15px',
                            borderBottom: '2px solid #333',
                            paddingBottom: '10px'
                        }}>
                            {entry.title}
                        </h3>

                        {/* DATE: TOP RIGHT */}
                        <span style={{
                            position: 'absolute',
                            top: '15px',
                            right: '20px',
                            color: '#000000',
                            fontSize: '0.8rem',
                            fontWeight: '500'
                        }}>
                            {new Date(entry.createdAt).toLocaleDateString()}
                        </span>

                        {/* 2. Left-aligned Text */}
                        <p style={{
                            textAlign: 'left',
                            lineHeight: '1.6',
                            whiteSpace: 'pre-wrap',
                            color: '#000000',
                            minHeight: '100px' // Ensures small notes don't look squished
                        }}>
                            {entry.notes}
                        </p>

                        {/* RATING: TOP LEFT */}
                        <div style={{
                            position: 'absolute',
                            top: '15px',
                            left: '20px',
                            fontSize: '0.9rem',
                            color: '#333',
                            fontWeight: 'bold'
                        }}>
                            {[...Array(10)].map((_, index) => (
                                <span key={index} style={{
                                    color: index < entry.ratings ? '#ffc107' : '#e0e0e0',
                                    fontSize: '1rem'
                                }}>★</span>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default GameJournalPage;