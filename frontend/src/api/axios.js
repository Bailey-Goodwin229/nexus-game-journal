import axios from 'axios' // Axios is HTTP Client

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

export default api; // Makes this 'Bean' available to other files