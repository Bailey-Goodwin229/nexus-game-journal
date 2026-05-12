import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectRoute from './components/ProtectRoute';
import Login from './components/Login';
import Register from './components/Register';
import JournalFeed from './components/JournalFeed';
import GameJournalPage from './components/GameJournalPage';

function App() {
    return (
        <AuthProvider>
            <Router>
                <div className="App">
                    <Routes>
                        {/* Public Routes */}
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />

                        {/* Protected Routes - Wrapped in our new Guard */}
                        <Route path="/journal" element={
                            <ProtectRoute>
                                <JournalFeed />
                            </ProtectRoute>
                        } />

                        <Route path="/journal/:gameTitle" element={
                            <ProtectRoute>
                                <GameJournalPage />
                            </ProtectRoute>
                        } />

                        {/* Default Redirect */}
                        <Route path="/" element={<Navigate to="/journal" />} />
                    </Routes>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;
