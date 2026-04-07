import { useState, useEffect } from 'react';
import { searchGames } from '../services/gameService';
import api from '../api/axios';

const AddEntryForm = ({ onEntryAdded }) => {

    // 1. Staging State: for the text box
    const [searchTerm, setSearchTerm] = useState('');
    const [searchResults, setSearchResults] = useState([]);

    // 2. Data State: what actually gets saved to DB
    const [formData, setFormData] = useState({
        title: '',
        gameTitle: '',
        twitchId: '',
        ratings: 5,
        notes: ''
    });

    // Debounce Logic
    useEffect(() => {
        // Optimization: Don't call the API if the user only typed 1 or 2 letters
        if (searchTerm.length < 3) {
            setSearchResults([]);
            return;
        }

        // Set a timer to wait 500ms
        const timer = setTimeout(async () => {
            try {
                const results = await searchGames(searchTerm);
                setSearchResults(results);
            } catch (err) {
                console.error("Search Failed: ", err);
            }
        }, 500);

        // Cleanup: If the user types again before 500ms, kill the old timer
        return () => clearTimeout(timer);
    }, [searchTerm]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // POSTing to your @PostMapping endpoint in JournalController
            const response = await api.post('/journal/save', formData);

            // Clear the form on success
            setFormData({ title: '', gameTitle: '', ratings: 5, notes: '' });

            // Tell the parent (JournalFeed) to refresh the list!
            onEntryAdded(response.data);
        } catch (err) {
            console.error("Failed to add entry:", err);
            alert("Error saving your entry. Check the console!");
        }
    };

    return (
        <form className="add-entry-form" onSubmit={handleSubmit}>
            <div className="search-section">
                <input
                    type="text"
                    placeholder="Search for a game..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    />

                {/* 1. Display Search Results Dropdown */}
                {searchResults.length > 0 && (
                    <ul className="search-results-dropdown">
                      {searchResults.map((game) => (
                          <li key={game.twitchId} onClick={() => {
                              // 2. AUTO-FILL Logic: When they click, fill the form!
                              setFormData({
                                  ...formData,
                                  gameTitle: game.title,
                                  twitchId: game.twitchId,
                                  coverArtUrl: game.coverArtUrl
                              });
                              setSearchTerm(game.title); // Update text box
                              setSearchResults([]); // Close the dropdown
                          }}>
                              {game.title}
                          </li>
                      ))}
                  </ul>
                )}
            </div>
            <input
                type='text'
                placeholder="Entry Title (e.g. Masterpiece!)"
                value={formData.title}
                onChange={(e) => setFormData({...formData, title: e.target.value})}
                required
            />
            <div className="rating-input">
                <label>Rating: {formData.ratings}/10</label>
                <input
                    type="range" min="1" max="10"
                    value={formData.ratings}
                    onChange={(e) => setFormData({...formData, ratings: parseInt(e.target.value)})}
                />
            </div>
            <textarea
                placeholder="Your thoughts..."
                value={formData.notes}
                onChange={(e) => setFormData({...formData, notes: e.target.value})}
            />
            <button type="submit">Deploy to Journal</button>
        </form>
    );
};

export default AddEntryForm;