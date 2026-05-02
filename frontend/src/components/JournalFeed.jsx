import React, { useEffect, useState } from 'react';
import api from '../api/axios'; // Specialized Axios instance that handles the JWT (login token) automatically.
import { Link, useNavigate } from 'react-router-dom'; // This is a "Smart Link." Instead of refreshing the whole website like a normal link, it just swaps the content instantly.
import AddEntryForm from './AddEntryForm';// Our "Senior" Axios instance with the Interceptor
import { searchGames } from '../services/gameService';

/*
 * This file is the "Main Menu" of your archive.
 * It fetches everything, organizes it by game title, and creates clickable links so you can dive into specific game journals.
 */
const GameSection = ({ gameTitle, entries }) => {

    // Grab the cover art from the first entry in this specific group
    const coverArt = entries[0]?.coverArtUrl;

    return (
        <div className="game-section" style={{ marginBottom: '40px' }}>
            {/* Navigates to /journal/GameName */}
            <Link
                /* This is crucial. If a game is called "Elden Ring," it turns the space into %20 so the browser doesn't break when it looks at the URL. */
                to={`/journal/${encodeURIComponent(gameTitle)}`}
                style={{ textDecoration: 'none', color: 'inherit' }}
            >
                <div className="game-header-toggle" style={{ display: 'flex', alignItems: 'center', gap: '20px', cursor: 'pointer' }}>
                    {coverArt && <img src={coverArt} alt={gameTitle} style={{ width: '80px', borderRadius: '8px' }} />}
                    <h2>{gameTitle} ({entries.length})</h2>
                </div>
            </Link>
        </div>
    );
};

const JournalFeed = () => {

    const navigate = useNavigate(); // For switching between pages
    const [searchTerm, setSearchTerm] = useState('');
    const [searchResults, setSearchResults] = useState([]);

    // Existing entries
    const [entries, setEntries] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Logout Logic
    const handleLogout = () => {
        localStorage.removeItem('token'); // Clears the keycard
        navigate('/login'); // Send user back to the gate
    };

    // Clear Search
    const handleClearSearch = () => {
        setSearchTerm('');
        setSearchResults([]);
    }

    useEffect(() => {
        if (searchTerm.length < 3) {
            setSearchResults([]);
            return;
        }

        const timer = setTimeout(async () => {
            try {
                const results = await searchGames(searchTerm);
                setSearchResults(results);
            } catch (err) {
                console.error("Search Failed: ", err);
                setSearchResults([]);
            }
        }, 500);

        return () => clearTimeout(timer);
    }, [searchTerm]);

    {/* As soon as the page loads, it asks your Spring Boot backend for all journal entries. */}
    useEffect(() => {
        const fetchJournal = async () => {
            try {
                {/* Once the data arrives, it saves it into the entries state, which triggers the rest of the code to run. */}
                const response = await api.get('/journal');
                setEntries(response.data);
                setLoading(false);
            } catch (err) {
                const backendMessage = err.response?.data?.error;
                setError(backendMessage || "The archives are unreachable. Please try again later.");
                setLoading(false);

                // If the token is expired (401), send them to login automatically
                if (err.response?.status === 401) {
                    localStorage.removeItem('token');
                    navigate('/login');
                }
            }
        };
        fetchJournal();
    }, []);

    // Organizes data from the journal and groups it by game
    // Sorts games and puts them into buckets, if games have the same name its places them into their own object.
    const groupedEntries = entries.reduce((groups, entry) => {
        const game = entry.gameTitle || "Uncategorized";
        if (!groups[game]) groups[game] = [];
        groups[game].push(entry);
        return groups;
    }, {});

    if (loading) return <div className="nexus-loader">Scanning The Nexus...</div>;
    if (error) return (
        <div className"nexus-error" style={{ textAlign: 'center', marginTop: '50px' }}>
            <h2 style={{ fontFamily: 'cursive' }}>⚠️ Entry Blocked</h2>
            <p>{error}</p>
            <button
                className="nav-tab"
                style={{ position: 'static' }}
                onClick={() => window.location.reload()}
            >
                Try to Re-open Archive
            </button>
        </div>
    );

    return (
        <div className="journal-feed">
            <div>
                <button onClick={handleLogout} className="nav-tab logout-btn">
                    Close Journal
                </button>
            </div>
            <h1>Your Gaming Archive</h1>
            {/* Navigation Search Section */}
            <div className="search-section" style={{ position: 'relative', maxWidth: '600px', margin: '0 auto' }}>
                <input
                    type="text"
                    placeholder="Search for a game..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="ruled-line-input"
                    style={{ width: '100%', paddingRight: '40px' }}
                    />
                {/* Only show the X if there is text in the box */}
                {searchTerm && (
                    <button
                        onClick={handleClearSearch}
                        className="clear-search-btn"
                        style={{
                            position: 'absolute',
                            right: '10px',
                            top: '50%',
                            transform: 'translateY(-50%)',
                            background: 'none',
                            border: 'none',
                            fontSize: '1.2rem',
                            cursor: 'pointer',
                            color: '#888'
                        }}
                    >
                        ✕
                    </button>
                )}
                {searchTerm.length >= 3 && (
                    <ul className="search-results-dropdown">
                        {searchResults.length > 0 ? (
                            searchResults.map((game) => (
                                <li key={game.twitchId} onClick={() => navigate(`/journal/${encodeURIComponent(game.title)}`, { state: { twitchId: game.twitchId, coverArtUrl: game.coverArtUrl } })}>
                                    {game.title}
                                </li>
                            ))
                        ) : (
                            /* THEMATIC EMPTY STATE */
                            <li className="no-results-msg" style={{ padding: '15px', color: '#888', fontStyle: 'italic', cursor: 'default' }}>
                                "This title is missing from the Nexus records..."
                            </li>
                        )}
                    </ul>
                )}
            </div>

            <div className="entries-container" style={{ marginTop: '40px' }}>
                {/* Since we can't loop through an object directly, we get a list of the Game Titles (the keys) and loop through those instead. */}
                {Object.keys(groupedEntries).length > 0 ? (
                    /* For every game title it finds (like "Halo"), it creates one of your GameSection components and passes it the title and all the entries for that game. */
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