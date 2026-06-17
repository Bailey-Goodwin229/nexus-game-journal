// This code configures and exports a global Axios HTTP client instance in a Vite-based
// frontend project (like React or Vue) designed to handle cross-origin api requests securely.
import axios from 'axios'; // Imports the Axios library to handle HTTP requests

// Grabs the backend server URL from Vite’s environment variables.
// If it is not defined, it defaults to your local development server at http://localhost:8080.
const rawBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
// A safety check that inspects the URL string. If it doesn't end with /api,
// it appends it automatically to match your Spring Boot controller's base path (/api/auth`).
const cleanBaseUrl = rawBaseUrl.endsWith('/api') ? rawBaseUrl : `${rawBaseUrl}/api`;

// Instantiates a custom instance of Axios with shared, pre-configured settings.
const api = axios.create({
    // Automatically prefixes every request made with this instance (e.g., calling api.post('/auth/login')
    // targets ${cleanBaseUrl}/auth/login).
    baseURL: cleanBaseUrl,
    // Crucial security setting. Instructs the browser to automatically include cookies (like your secure nexus_token) in cross-origin requests.
    // Without this, your Spring Boot backend will never receive the HttpOnly auth cookie.
    withCredentials: true,
});

// Exports this configured instance so you can import and use it across your entire frontend codebase.
export default api;