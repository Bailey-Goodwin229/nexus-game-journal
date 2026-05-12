import axios from 'axios' // Axios is HTTP Client

/*
This is your Axios Interceptor file.
It’s like a "security checkpoint" that every single communication between your React app and your Spring Boot backend must pass through.
 */

// Create an 'instance'
const api = axios.create({
    // Gets the URL from our .env file.
    baseURL: import.meta.env.VITE_API_BASEURL || 'http://localhost:8080/api',
    withCredentials: true,
});

export default api; // Makes this 'Bean' available to other files