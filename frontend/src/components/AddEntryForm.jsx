import { useState } from 'react';
import api from '../api/axios';

const AddEntryForm = ({ onEntryAdded }) => {
    const [formData, setFormData] = useState({
        title: '',
        gameTitle: '',
        ratings: 5,
        notes: ''
    });

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
            consoole.error("Failed to add entry:", err);
            alert("Error saving your entry. Check the console!");
        }
    };

    return (
        <form className="add-entry-form" onSubmit={handleSubmit}>
            <h3>Log a New Session</h3>
            <input
                type='text'
                placeholder="Entry Title (e.g. Masterpiece!)"
                value={formData.title}
                onChange={(e) => setFormData({...formData, title: e.target.value})}
                required
            />
            <input
                type='text'
                placeholder="Game Title"
                value={formData.gameTitle}
                onChange={(e) => setFormData({...formData, gameTitle: e.target.value})}
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

export default AddEntryForm