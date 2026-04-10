import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import JournalFeed from './components/JournalFeed';
import GameJournalPage from './components/GameJournalPage';

// 1. The "GateKeeper" (Guard)
// This functions checks the "Keycard" (token) before letting someone through the door.
const ProtectRoute = ({ children}) => {
    const token = localStorage.getItem('token');
    // If no token exists, kick them back to /login. Otherwise, show the page.
    return token ? children : <Navigate to="/login" />;
};


function App() {
  return (
      <Router>
        <div className="App">
          <Routes>
              {/* Public Route: Anyone can see the login page */}
            <Route path="/login" element={<Login />} />

              {/* 2. PROTECTED ROUTE: The Journal Feed */}
              {/* We wrap the component inside our Guard. */}
            <Route
                path="/journal"
                element={
                    <ProtectRoute>
                        <JournalFeed />
                    </ProtectRoute>
                }
            />

              {/* Protected route for individual game pages */}
              <Route
                  path="/journal/:gameTitle"
                  element={
                  <ProtectRoute>
                      <GameJournalPage />
                  </ProtectRoute>
                  }

              {/* Redirect the root '/' to '/journal'.
              The Guard will catch them if they aren't logged in! */}
              <Route path="/" element={<Navigate to="/journal" />} />
          </Routes>
        </div>
      </Router>
  );
}

export default App;
