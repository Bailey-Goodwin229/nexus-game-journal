import { useState, useEffect } from 'react';
import api from '../api/axios';
import '../App.css';

// Defines the component; it receives onEntryAdded, a function to tell the parent component to refresh when a new entry is saved.
const AddEntryForm = ({ onEntryAdded }) => {

    // 1. Staging State: for the text box
    // searchTerm tracks what the user types in the search box; searchResults holds the list of games returned by the API.
    const [searchTerm, setSearchTerm] = useState('');
    const [searchResults, setSearchResults] = useState([]);

    // 2. Data State: what actually gets saved to DB
    // is the "source of truth" for the final database entry, including the entry title, the selected game's metadata, a 1-10 rating, and notes.
    const [formData, setFormData] = useState({
        title: '',
        gameTitle: '',
        twitchId: '',
        ratings: 5,
        notes: ''
    });

    // intercepts the standard form submission.
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Create a payload to match Entity structure
        const payload = {
            title: formData.title,
            ratings: formData.ratings,
            notes: formData.notes,
            // Wrap game details in a 'game' object to match @ManyToOne
            game: {
                title: formData.gameTitle,
                twitchId: Number(formData.twitchId),
                coverArtUrl: formData.coverArtUrl
            }
        };

        try {
            // POSTing to your @PostMapping endpoint in JournalController
            const response = await api.post('/journal/save', formData);

            // Clear the form on success
            setFormData({
                title: '',
                gameTitle: '',
                twitchId: '',
                ratings: 5,
                notes: '' });

            setSearchTerm('');

            // Tell the parent (JournalFeed) to refresh the list!
            onEntryAdded(response.data);
        } catch (err) {
            console.error("Failed to add entry:", err);
            alert("Error saving your entry. Check the console!");
        }
    };

    return (
        /* 1. The search input updates searchTerm on every keystroke. */
        <form className="add-entry-form" onSubmit={handleSubmit}>
            {/* A standard text input for the Journal Title. */}
            <input
                type='text'
                placeholder="Journal Entry Title (e.g. Masterpiece!)"
                value={formData.title}
                onChange={(e) => setFormData({...formData, title: e.target.value})}
                required
            />

            {/* A Star Rating system. It creates an array of 10 elements and renders a star (★) for each. If you click the 7th star, formData.ratings becomes 7, and stars 1-7 turn gold. */}
            <div className="rating-input">
                <label>Rating: {formData.ratings}/10</label>
                <div className="star-container" style={{ display: 'flex', gap: '5px', cursor: 'pointer' }}>
                    {[...Array(10)].map((_, index) => {
                        const starValue = index + 1;
                        return (
                            <span
                                key={starValue}
                                onClick={() => setFormData({ ...formData, ratings: starValue })}
                                style={{
                                    fontSize: '2rem',
                                    color: starValue <= formData.ratings ? '#ffc107' : '#e4e5e9', // Gold for filled, gray for empty
                                    transition: 'color 0.2s'
                                }}
                            >
                                ★
                            </span>
                        );
                    })}
                </div>
            </div>
            {/* A textarea for the user's thoughts. */}
            <textarea
                placeholder="Your thoughts..."
                value={formData.notes}
                onChange={(e) => setFormData({...formData, notes: e.target.value})}
            />
            {/* The submit button that triggers handleSubmit. */}
            <button type="submit">Deploy to Journal</button>
        </form>
    );
};

export default AddEntryForm;