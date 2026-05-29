import axios from 'axios';

// 1. Get the raw URL from the environment, defaulting to localhost if missing
const rawBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

// 2. Clean up the URL to prevent double '/api/api' paths completely
const cleanBaseUrl = rawBaseUrl.endsWith('/api') ? rawBaseUrl : `${rawBaseUrl}/api`;

const api = axios.create({
    baseURL: cleanBaseUrl,
    withCredentials: true,
});

// Force global authorization headers to carry credential tokens cross-domain
axios.defaults.withCredentials = true;

export default api;