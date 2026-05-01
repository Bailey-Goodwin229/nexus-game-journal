import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/authService';

const Login = () => {
    // 'useState' is how we create variables that "refresh" the HTML when they change.
    const [credentials, setCredentials] = useState({ username: '', password: '' })
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // Controller method
    const handleSubmit = async (e) => {
        e.preventDefault();
        // trys to log in using credentials
        try {
            await login(credentials.username, credentials.password);
            navigate('/journal'); // Success! Send them to the feed
        } catch (err) {
            const errorMessage = err.response?.data?.error || "A connection error occurred.";
            alert(errorMessage);
        }
    };

    return (
        // sets the class
        <div className="login-container">
            {/* onSubmit maps the form's 'Enter' key or Button click to our Java-style Controller method */}
            <form onSubmit={handleSubmit}>
                <h2>Nexus Gaming Login</h2>
                {/* Short-circuit evaluation: If 'error' is not empty, render the <p> tag. Like an 'if' statement in Thymeleaf. */}
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <input
                    type="text"
                    placeholder="Username"
                    /* (e) is the event. setCredentials creates a NEW object, copying existing fields (...)
                    and overwriting 'username' with the current text box value. */
                    onChange={(e) => setCredentials({...credentials, username: e.target.value})}
                />
                <input
                    type="password"
                    placeholder="Password"
                    /* Same logic: Keep the username we just typed, but update the 'password' field in the state object */
                    onChange={(e) => setCredentials({...credentials, password: e.target.value})}
                />
                {/* In a form, the 'submit' type automatically triggers the 'onSubmit' handler in the <form> tag above */}
                <button type="submit">Unlock Journal</button>
                <p>New to the journal? <a href="/register">Sign the Guestbook</a></p>
            </form>
        </div>
    );
};
// This makes the Login component "Public" so App.jsx can import and display it.
export default Login;