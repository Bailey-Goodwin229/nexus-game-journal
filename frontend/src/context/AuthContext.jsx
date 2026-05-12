import { createContext, useState, useEffect, useContext } from 'react';
import { checkSession } from '../services/authService';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    // 1. Isolate the validation logic so components can manually call it
    const refreshUser = async () => {
        try {
            const username = await checkSession();
            if (username) {
                setUser({username});
            } else {
                setUser(null);
            }
        } catch (err) {
            setUser(null);
        } finally {
            setLoading(false);
        }
    };

    // 2. Run it automatically when the app boots up or refreshes
    useEffect(() => {
        refreshUser();
    }, []);

    return (
        // 3. CRITICAL: Add refreshUser to the provider value list
        <AuthContext.Provider value={{ user, setUser, loading, refreshUser }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);