import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import { useUser } from '../../context/UserContext';
import PrivateRoutesApi from '../../services/PrivateRoutesApi';

const StudentPrivateRoute = ({ children }) => {
    const isAuthenticated = !!localStorage.getItem('jwtToken');
    const { user, setUser } = useUser();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const setupUser = async () => {
            const token = localStorage.getItem('jwtToken');
            if (token && !user) {
                try {
                    const userResponse = await PrivateRoutesApi.currentUser(token);
                    setUser(userResponse.data);
                } catch (error) {
                    console.error('Failed to fetch current user', error);
                } finally {
                    setLoading(false);
                }
            } else {
                setLoading(false);
            }
        };
        setupUser();
    }, [user, setUser]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!isAuthenticated) return <Navigate to="/login" />;
    
    if (user && user.role !== 'student') return <Navigate to="/not-authorized" />;

    return children;
};

export default StudentPrivateRoute;

