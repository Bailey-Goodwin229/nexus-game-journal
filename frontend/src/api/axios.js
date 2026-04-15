import axios from 'axios' // Axios is HTTP Client

/*
This is your Axios Interceptor file.
It’s like a "security checkpoint" that every single communication between your React app and your Spring Boot backend must pass through.
 */

// Create an 'instance'
const api = axios.create({
    // Gets the URL from our .env file.
    baseURL: import.meta.env.VITE_API_BASEURL || 'http://localhost:8080/api',
});

// automatically attach the JWT "Keycard" to every request
// This is the "Interceptor" (A Client-Side Filter)
api.interceptors.request.use((config) => {
    // 1. Look in the browser's "LocalStorage" (a mini database in Chrome) for our token
    const token = localStorage.getItem('token');
    // 2. if it exists, add it to the HTTP Header
    if (token){
        config.headers.Authorization = `Bearer ${token}`;
    }
    // 3. Let the request continue to the server
    return config;
});

// Add a RESPONSE interceptor
api.interceptors.response.use(
    (response) => response, // If the response is good, just return it
    (error) => {
        // If the server returns 401 or 403, the token is likely expired
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            console.warn("Token expired or invalid. Redirecting to login...");
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/login'; // Force them to re-authenticate
        }
        return Promise.reject(error);
    }
);

export default api; // Makes this 'Bean' available to other files