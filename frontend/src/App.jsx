import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'; // Works with different rules related to the URL
import Login from './components/Login'; // Brings information from Login into the app
import JournalFeed from './components/JournalFeed'; // Brings information from JournalFeed into the app
import GameJournalPage from './components/GameJournalPage'; // Brings information from GameJournalPage into the app

// 1. The "GateKeeper" (Guard)
// This functions checks the "Keycard" (token) before letting someone through the door.
const ProtectRoute = ({ children}) => { // Higher-Oder Component that acts as a wrapper
    const token = localStorage.getItem('token'); // Looks for a keycard
    // If no token exists, kick them back to /login. Otherwise, show the page.
    return token ? children : <Navigate to="/login" />;
};

// This is the App Shell. Its job is to handle navigation and security—deciding who is allowed to see what.
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
                  />

              {/* Redirect the root '/' to '/journal'*/}
                  {/* The Guard will catch them if they aren't logged in! */}
              <Route path="/" element={<Navigate to="/journal" />} />
          </Routes>
        </div>
      </Router>
  );
}

export default App;
