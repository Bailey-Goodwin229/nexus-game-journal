import { useAuth } from '../context/AuthContext';
import { Navigate } from 'react-router-dom';

// This code is a React protected route guard component that secures specific client-side web pages,
// preventing unauthenticated users from viewing private dashboard or profile content.
const ProtectRoute = ({ children }) => {
    // Calls a custom React hook (likely wrapping a React Context) to extract the current authentication state and a loading flag.
    const { user, loading } = useAuth();

    // Halts rendering if the app is still fetching the user's profile from the backend /api/auth/me endpoint.
    // It prevents accidental redirects while the network request resolves.
    if (loading) return <div>Checking the Guestbook...</div>; // Aesthetic loader

    // If user exists: It returns and renders the private children components.
    // If user is null: It renders a <Navigate> component (from React Router) that forces the browser to redirect immediately to the /login page.
    return user ? children : <Navigate to="/login" />;
};

export default ProtectRoute;