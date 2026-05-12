import { useAuth } from '../context/AuthContext';
import { Navigate } from 'react-router-dom';

const ProtectRoute = ({ children }) => {
    const { user, loading } = useAuth();

    if (loading) return <div>Checking the Guestbook...</div>; // Aesthetic loader

    return user ? children : <Navigate to="/login" />;
};

export default ProtectRoute;