import api from '../api/axios';

// Makes this function public. 'async' means it won't block the UI while waiting.
export const login = async (username, password) => {
    // POST to your /auth/Login endpoint
    // 'await' tells JS: "Wait for the server to reply before moving to the next line."
    const response = await api.post('/auth/login', {username, password});

    // If successful return of 'AuthResponseDTO', save the "Keycard"
    if (response.data.token){
        // Save the token so the Interceptor (above) can find it later
        localStorage.setItem('token', response.data.token);
        // Save the user data as a string (LocalStorage only stores strings, not objects)
        localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data; // Return the DTO to the component
};

export const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login';
};