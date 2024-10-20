import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import './index.css';
import App from './App';
import { Toaster } from 'react-hot-toast';
import { UserProvider } from './context/UserContext';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter>
  <UserProvider>
    <App />
    <Toaster/>
  </UserProvider>
  </BrowserRouter>
);