import api from '../api/axios';

// Search games
export const  searchGames = async (query) => {
    // This hits the @GetMapping(/search) method in controller
    const response = await api.get(`/games/search?title=${gameName}`);
    return response.data; // returns a list of games
};

