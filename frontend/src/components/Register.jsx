import { useState } from 'react';
import axios from 'axios';

const Register = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        confirmPassword: ''
    });

    const handleRegister = async (e) => {
        e.preventDefault();

        // 1. Client-side "Confirm Password" check
        if (formData.password !== formData.confirmPassword) {
            alert("Passwords do not match.");
            return;
        }

        try {
            // 2. Send only username and password to the backend
            const response = await axios.post('http://localhost:8080/api/auth/register', {
                username: formData.username,
                password: formData.password
            });

            // 3. Store the JWT token and redirect/alert
            localStorage.setItem('token', response.data.token);
            alert("Journal Created! Welcome to Nexus Gaming");
        } catch (err) {
            console.error(err);
            alert("Registration failed.");
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