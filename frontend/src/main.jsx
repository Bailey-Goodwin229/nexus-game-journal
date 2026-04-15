import { StrictMode } from 'react' // Helper component that activates extra checks and warnings to help fund bugs
import { createRoot } from 'react-dom/client' // Connects React to the web browser
import './index.css' // Applies CSS rules from the file
import App from './App.jsx' // Brings in items from main App, root of the component tree

// This is the entry point of the entire React application.
// It’s the very first bit of code that runs when your website loads.

createRoot(document.getElementById('root')).render( // Looks for index.html to find the hook
  <StrictMode>
    <App /> {/* It puts your App inside the StrictMode wrapper. Now, your entire project—including your Journal Feed and Game Pages—starts running. */}
  </StrictMode>,
)
