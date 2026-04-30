import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'; // Works with different rules related to the URL
import Login from './components/Login'; // Brings information from Login into the app
import JournalFeed from './components/JournalFeed'; // Brings information from JournalFeed into the app
import GameJournalPage from './components/GameJournalPage'; // Brings information from GameJournalPage into the app
import Register from './components/Register';

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
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />

              {/* Protected routes */}
              <Route path="/journal" element={<ProtectRoute><JournalFeed /></ProtectRoute>} />
              <Route path="/journal/:gameTitle" element={<ProtectRoute><GameJournalPage /></ProtectRoute>} />

              {/* ONE redirect for the root path - this sends guests to login and users to journal */}
              <Route path="/" element={<Navigate to="/journal" />} />
          </Routes>
        </div>
      </Router>
  );
}

export default App;
