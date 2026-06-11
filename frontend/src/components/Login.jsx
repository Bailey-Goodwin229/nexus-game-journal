import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/authService';
import { useAuth } from '../context/AuthContext';

const Login = () => {
    const [credentials, setCredentials] = useState({ username: '', password: '' })
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const { refreshUser } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {
            // 1. Grab the DTO data returning from your authService layer
            const data = await login(credentials.username, credentials.password);

            // 2. Extract and save the token if it is included inside your backend's DTO object
            if (data && data.token) {
                localStorage.setItem('nexus_token', data.token);
            }

            // 3. Tell the AuthContext to go fetch the user info
            await refreshUser();

            // 4. Navigate into your journal dashboard
            navigate('/journal');
        } catch (err) {
            const errorMessage =
                err.response?.data?.error ||
                err.response?.data?.message ||
                (typeof err.response?.data === 'string' ? err.response.data : null) ||
                "Incorrect username or password.";

            setError(errorMessage);
        }
    };

    return (
        <div className="login-container">
            <form onSubmit={handleSubmit}>
                <h2>Nexus Gaming Login</h2>
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <input
                    type="text"
                    placeholder="Username"
                    onChange={(e) => setCredentials({...credentials, username: e.target.value})}
                />
                <input
                    type="password"
                    placeholder="Password"
                    onChange={(e) => setCredentials({...credentials, password: e.target.value})}
                />
                <button type="submit">Unlock Journal</button>
                <p>New to the journal? <a href="/register">Sign the Guestbook</a></p>
            </form>
        </div>
    );
};

export default Login;