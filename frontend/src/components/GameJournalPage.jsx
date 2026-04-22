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

    // ✅ DERIVED STATE: This recalculates automatically whenever 'entries' changes
    const total = entries.reduce((acc, entry) => acc + (entry.ratings || 0), 0);
    const overallRating = entries.length > 0 ? (total / entries.length).toFixed(1) : 0;

    useEffect(() => {
        const fetchGameEntries = async () => {
            try {
                setLoading(true);
                // This calls your NEW backend endpoint
                const response = await api.get(`/journal/game/${encodeURIComponent(gameTitle)}`);

                // Set the entries directly from the backend response
                setEntries(response.data);
                setLoading(false);
            } catch (err) {
                console.error("Failed to load entries", err);
                setLoading(false);
            }
        };

        if (gameTitle) {
            fetchGameEntries();
        }
    }, [gameTitle]);

    if (loading) return <div style={{ textAlign: 'center', padding: '50px' }}>Loading your archive...</div>;

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
            <div style={{ textAlign: 'center', marginBottom: '30px' }}>
                {/* The Title */}
                <h1 style={{ marginBottom: '5px' }}>
                    {decodeURIComponent(gameTitle)}
                </h1>

                {/* The Stars and Rating underneath */}
                <div style={{
                    color: '#ffc107',
                    fontSize: '1.5rem',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    gap: '10px'
                }}>
                    <span>★</span>
                    <span style={{ fontWeight: 'bold' }}>{overallRating}/10</span>
                    <span style={{ color: '#888', fontSize: '1rem' }}>
            ({entries.length} {entries.length === 1 ? 'entry' : 'entries'})
                    </span>
                </div>
            </div>
            {/* --- NEW ENTRY DIVIDER --- */}
            <div style={{ display: 'flex', alignItems: 'center', margin: '40px 0 30px 0', color: '#888' }}>
                <div style={{ flex: 1, height: '1px', backgroundColor: '#333' }}></div>
                <span style={{ padding: '0 20px', fontSize: '0.9rem', fontWeight: 'bold', textTransform: 'uppercase', letterSpacing: '2px' }}>
                New Entry
            </span>
                <div style={{ flex: 1, height: '1px', backgroundColor: '#333' }}></div>
            </div>
            {/* 2. New Entry Section */}
            <div className="add-entry-section" style={{ marginBottom: '50px' }}>
                <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>New Journal Entry</h2>
                <AddEntryForm
                    preselectedGame={decodeURIComponent(gameTitle)}
                    twitchId={gameData.twitchId}
                    coverArtUrl={gameData.coverArtUrl}
                    onEntryAdded={(newEntry) => setEntries(prev => [newEntry, ...prev])}
                />
            </div>
            <div style={{ display: 'flex', alignItems: 'center', margin: '60px 0 40px 0', color: '#888' }}>
                <div style={{ flex: 1, height: '1px', backgroundColor: '#333' }}></div>
                <span style={{ padding: '0 20px', fontSize: '0.9rem', fontWeight: 'bold', textTransform: 'uppercase', letterSpacing: '2px' }}>
                        Past Entries
                </span>
                <div style={{ flex: 1, height: '1px', backgroundColor: '#333' }}></div>
            </div>
            <div className="entries-grid">
                {entries.map(entry => {
                    // 1. Calculate logic before returning the UI
                    const isEditing = editingId === entry.journalId;

                    // 2. Now explicitly return the UI
                    return (
                        <div key={entry.journalId} className="game-card"
                             style={{padding: '30px', marginBottom: '20px', position: 'relative'}}>
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
                })}
            </div>
        </div>
    );
};
export default GameJournalPage;