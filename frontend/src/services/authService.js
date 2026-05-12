import api from '../api/axios';

// Makes this function public. 'async' means it won't block the UI while waiting.
// 1. Login: No more manual token saving!
export const login = async (username, password) => {
    // The backend now sends the 'Set-Cookie' header.
    // Because we set 'withCredentials: true' in axios, the browser saves it automatically.
    const response = await api.post('/auth/login', {username, password});

    return response.data; // Return the DTO to the component
};

// 2. Logout: Tell the server to "delete" the cookie
export const logout = async () => {
    try {
        // We MUST call the backend logout to expire the HttpOnly cookie
        await api.post('/auth/logout');
    } catch (err) {
        console.error("Logout failed on server, but clearing local state anyway.");
    } finally {
        // Clear any UI-only state and redirect
        window.location.href = '/login';
    }
};

// 3. The "Am I logged in?" check
export const checkSession = async () => {
    // This calls the @GetMapping("/auth/me")
    const response = await api.get('/auth/me');
    return response.data;
}