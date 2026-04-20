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
    // Update logic
    const [editingId, setEditingId] = useState(null); // Tracks which card is being edited
    const [editData, setEditData] = useState({}); // Holds temporary change


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

     // Delete logic
     const handleDelete = async (journalId) => {
         // A safety check to make sure you want to delete the item
         if (!window.confirm("Are you sure you would like to delete this?")) return;

         try {
             await api.delete(`/journal/${journalId}`);
             // Update the UI: Filter out the deleted entry so it disappears immediately
             setEntries(prevEntries => prevEntries.filter(entry => entry.journalId !== journalId));
         } catch (err) {
             console.error("Failed to delete entry:", err);
             alert("Could not delete the entry. Try again!");
         }
     };

     // Update logic
     const handleUpdate = async (journalId) => {
             try {
                 const response = await api.put(`/journal/${journalId}`, editData);
                 // Update the list with the new data and close edit mode
                 setEntries(prev => prev.map(entry => entry.journalId === journalId ? response.data : entry));
                 setEditingId(null);
             } catch (err) {
                 console.error("Update failed:", err);
             }
     };

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
                {entries.map(entry => {
                    // 1. Calculate logic before returning the UI
                    const isEditing = editingId === entry.journalId;

                    // 2. Now explicitly return the UI
                    return (
                        <div key={entry.journalId} className="game-card"
                             style={{padding: '30px', marginBottom: '20px', position: 'relative'}}>
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

                            {isEditing ? (
                                /* Edit Mode UI */
                                <div style={{display: 'flex', flexDirection: 'column', gap: '10px', marginTop: '20px'}}>
                                    <input
                                        value={editData.title}
                                        onChange={(e) => setEditData({...editData, title: e.target.value})}
                                        style={{textAlign: 'center'}}
                                    />
                                    <textarea
                                        value={editData.notes}
                                        onChange={(e) => setEditData({...editData, notes: e.target.value})}
                                        rows={8} // Increase this number for more starting space
                                        style={{
                                            width: '100%',
                                            padding: '10px',
                                            lineHeight: '1.6',
                                            fontSize: '1.1rem'
                                        }}
                                    />
                                    <button onClick={() => handleUpdate(entry.journalId)}>Save</button>
                                    <button onClick={() => setEditingId(null)}>Cancel</button>
                                </div>
                            ) : (
                                /* View Mode UI (Your current Title & Notes) */
                                <>
                                    <h3 style={{textAlign: 'center'}}>{entry.title}</h3>
                                    <p style={{
                                        textAlign: 'left',
                                        lineHeight: '1.6',
                                        whiteSpace: 'pre-wrap',
                                        color: '#000000',
                                        minHeight: '100px'
                                    }}>
                                        {entry.notes}</p>
                                    <button
                                        onClick={() => {
                                            setEditingId(entry.journalId);
                                            setEditData(entry);
                                        }}
                                        style={{
                                            position: 'absolute',
                                            bottom: '15px',
                                            left: '20px',
                                            border: 'none',
                                            background: 'none',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        ✎
                                    </button>
                                </>
                            )}

                            {/* 4. DELETE BUTTON: Bottom Right */}
                            <button
                                onClick={() => handleDelete(entry.journalId)}
                                style={{
                                    position: 'absolute',
                                    bottom: '15px',
                                    right: '20px',
                                    background: 'none',
                                    border: 'none',
                                    color: '#ff4d4d', // Nice "alert" red
                                    cursor: 'pointer',
                                    fontSize: '0.8rem',
                                    fontWeight: 'bold',
                                    opacity: '0.6',
                                    transition: 'opacity 0.2s'
                                }}
                                onMouseOver={(e) => e.target.style.opacity = '1'}
                                onMouseOut={(e) => e.target.style.opacity = '0.6'}
                            >
                                🗑️
                            </button>
                        </div>
                    );
                })};
            </div>
        </div>
    );
};

export default GameJournalPage;