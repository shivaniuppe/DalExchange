import React from 'react';
import { Link } from 'react-router-dom';

const NotAuthorizedPage = () => {
  const containerStyle = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    height: '100vh',
    textAlign: 'center',
    backgroundColor: '#f9f9f9',
  };

  const headingStyle = {
    fontSize: '2.5rem',
    marginBottom: '1rem',
  };

  const paragraphStyle = {
    fontSize: '1.2rem',
    marginBottom: '2rem',
  };

  const linkStyle = {
    fontSize: '1rem',
    color: '#fff',
    textDecoration: 'none',
    backgroundColor: '#000',
    padding: '0.5rem 1rem',
    borderRadius: '5px',
    transition: 'background-color 0.3s, color 0.3s',
  };

  const linkHoverStyle = {
    backgroundColor: '#333',
    color: '#fff',
  };

  return (
    <div style={containerStyle}>
      <h1 style={headingStyle}>403 - Not Authorized</h1>
      <p style={paragraphStyle}>You do not have permission to access this page.</p>
      <Link 
        to="/" 
        style={linkStyle}
        onMouseOver={(e) => {
          e.target.style.backgroundColor = linkHoverStyle.backgroundColor;
          e.target.style.color = linkHoverStyle.color;
        }}
        onMouseOut={(e) => {
          e.target.style.backgroundColor = linkStyle.backgroundColor;
          e.target.style.color = linkStyle.color;
        }}
      >
        Go back to Home
      </Link>
    </div>
  );
};

export default NotAuthorizedPage;
