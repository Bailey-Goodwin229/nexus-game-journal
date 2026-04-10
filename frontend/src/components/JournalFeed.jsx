import React, { useEffect, useState } from 'react';
import api from '../api/axios';
import AddEntryForm from './AddEntryForm';// Our "Senior" Axios instance with the Interceptor

/**
 * This gives each game its own independent "isOpen" state.
 */
const GameSection = ({ gameTitle, entries }) => {
    const [isOpen, setIsOpen] = useState(false);

    // Grab the cover art from the first entry in this specific group
    const coverArt = entries[0]?.coverArtUrl;

    return (
        <div className="game-section" style={{ marginBottom: '40px' }}>
            {/* The clickable area */}
            <div
                className="game-header-toggle"
                onClick={() => setIsOpen(!isOpen)}
                style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '20px', borderBottom: '2px solid #444', paddingBottom: '10px' }}
            >
                {coverArt && (
                    <img src={coverArt} alt={gameTitle} style={{ width: '80px', borderRadius: '8px' }} />
                )}
                <div>
                    <h2 style={{ margin: 0 }}>{gameTitle}</h2>
                    <small>{isOpen ? '▲ Hide Journals' : '▼ View Journals'} ({entries.length})</small>
                </div>
            </div>

            {/* Only show entries when the image/header is clicked */}
            {isOpen && (
                <div className="entries-grid" style={{ marginTop: '20px' }}>
                    {entries.map((entry) => (
                        <div key={entry.journalId} className="game-card">
                            <h3 className="entry-title">{entry.title}</h3>
                            <div className="rating">
                                Rating: {[...Array(10)].map((_, index) => (
                                <span key={index} style={{ color: index < entry.ratings ? '#ffc107' : '#444' }}>★</span>
                            ))}
                            </div>
                            <p className="notes">{entry.notes}</p>
                            <small>Logged on: {entry.createdAt ? new Date(entry.createdAt).toLocaleDateString() : 'Pending...'}</small>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

const JournalFeed = () => {
    const [entries, setEntries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchJournal = async () => {
            try {
                const response = await api.get('/journal');
                setEntries(response.data);
                setLoading(false);
            } catch (err) {
                setError("Could not load your journal.");
                setLoading(false);
            }
        };
        fetchJournal();
    }, []);

    // Organizes data from the journal and groups it by game
    const groupedEntries = entries.reduce((groups, entry) => {
        const game = entry.gameTitle || "Uncategorized";
        if (!groups[game]) groups[game] = [];
        groups[game].push(entry);
        return groups;
    }, {});

    if (loading) return <div className="nexus-loader">Scanning The Nexus...</div>;
    if (error) return <div className="nexus-error">{error}</div>;

    return (
        <div className="journal-feed">
            <h1>Your Gaming Archive</h1>
            {/* Pass a function to refresh the feed when a new entry is added */}
            <AddEntryForm onEntryAdded={(newEntry) => setEntries([newEntry, ...entries])} />

            <div className="entries-container" style={{ marginTop: '40px' }}>
                {Object.keys(groupedEntries).length > 0 ? (
                    Object.keys(groupedEntries).map((gameTitle) => (
                        <GameSection
                            key={gameTitle}
                            gameTitle={gameTitle}
                            entries={groupedEntries[gameTitle]}
                        />
                    ))
                ) : (
                    <p>No entries found. Time to play some games!</p>
                )}
            </div>
        </div>
    );
};

export default JournalFeed;