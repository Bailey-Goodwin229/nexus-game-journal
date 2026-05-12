import { useState } from 'react';
import api from '../api/axios';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';


const Register = () => {
    const navigate = useNavigate();
    const { refreshUser } = useAuth(); // Grab the refresh function
    const [formData, setFormData] = useState({ username: '', password: '', confirmPassword: '' });

    const handleRegister = async (e) => {
        e.preventDefault();

        if (formData.password !== formData.confirmPassword) {
            alert("Passwords do not match.");
            return;
        }

        try {
            // 1. Use the 'api' instance so 'withCredentials: true' is sent
            // This ensures the browser accepts the Set-Cookie header from Spring
            await api.post('/auth/register', {
                username: formData.username,
                password: formData.password
            });

            // 2. Sync the React state with the new cookie
            await refreshUser();

            alert("Journal Created! Welcome to Nexus Gaming");
            navigate('/journal');

        } catch (err) {
            if (err.response && err.response.status === 409) {
                alert(err.response.data || "That alias is already in the archive.");
            } else {
                alert("The ink is dry. Could not register at this time.");
            }
        }
    };

    return (
        <div className="diary-container">
            <div className="diary-page">
                <h2 style={{ fontFamily: 'cursive', textAlign: 'center' }}>
                    New Journal Participant
                </h2>
                <p style={{ textAlign: 'center', fontSize: '0.9rem', color: '#555' }}>
                    Sign the guestbook to start your journey.
                </p>
                <button
                    onClick={() => navigate('/login')}
                    className="nav-tab pos-left"
                    style={{
                        position: 'fixed',
                        top: '20px',
                        left: '20px',
                        zIndex: 1000
                    }}
                >
                    Back to Login
                </button>

                <form onSubmit={handleRegister} className={"register-form"}>
                    <div className="input-group">
                        <input
                            type="text"
                            placeholder="Desired Username"
                            className="ruled-line-input"
                            value={formData.username}
                            onChange={(e) => setFormData({...formData, username: e.target.value})}
                            required
                        />
                    </div>

                    <div className="input-group">
                        <input
                            type="password"
                            placeholder="Password"
                            className="ruled-line-input"
                            value={formData.password}
                            onChange={(e) => setFormData({...formData, password: e.target.value})}
                            required
                        />
                    </div>

                    <div className="input-group">
                        <input
                            type="password"
                            placeholder="Confirm Password"
                            className="ruled-line-input"
                            value={formData.confirmPassword}
                            onChange={(e) => setFormData({...formData, confirmPassword: e.target.value})}
                            required
                        />
                    </div>

                    <button type="submit" className="diary-button">
                        Sign the Guestbook
                    </button>
                </form>
            </div>
        </div>
    );
};

export default Register;