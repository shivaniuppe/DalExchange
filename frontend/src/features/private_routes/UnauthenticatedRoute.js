import React from 'react';
import { Navigate } from 'react-router-dom';

const UnauthenticatedRoute = ({ children }) => {
    const isAuthenticated = !!localStorage.getItem('jwtToken');
    return !isAuthenticated ? children : <Navigate to="/products" />;
};

export default UnauthenticatedRoute;
