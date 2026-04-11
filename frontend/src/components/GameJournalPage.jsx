import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api/axios';

const GameJournalPage = () => {
    const { gameTitle } = useParams(); // Grabs the title from the URl
    const [entries, setEntries] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchGameEntries = async () => {
            try {
                // You can either fetch all and filter, or hit a specific endpoint
                const response = await api.get('/journal');
                const filtered = response.data.filter(e => e.gameTitle === decodeURIComponent(gameTitle));
                setEntries(filtered);
                setLoading(false);
            } catch (err) {
                console.error("Failed to load entries", err);
                setLoading(false);
            }
        };
        fetchGameEntries();
    }, [gameTitle]);

    if (loading) return <div>Loading the Archive...</div>;

    return (
        <div className="game-detail-page" style={{ padding: '20px' }}>
            <Link to='/journal' style={{ color: '#00cf5d' }}>← Back to Archive</Link>
            <h1>{decodeURIComponent(gameTitle)}</h1>

            <div className="entries-grid">
                {entries.map(entry => (
                    <div key={entry.journalId} className="game-card">
                        <h3>{entry.title}</h3>
                        <p>{entry.notes}</p>
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