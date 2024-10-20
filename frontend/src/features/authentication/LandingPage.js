import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../../components/Header';

const LandingPage = () => {
  const headerConfig = {
    search: false,
    requests: false,
    notifications: false,
    add: false,
    profile: false,
  };

  const buttonStyles = {
    base: {
      display: 'inline-flex',
      height: '2.25rem',
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: '0.375rem',
      padding: '0 1rem',
      fontSize: '0.875rem',
      fontWeight: '500',
      transition: 'background-color 0.2s',
      textDecoration: 'none',
    },
    loginHeader: {
      backgroundColor: 'white',
      color: 'black',
      border: '1px solid black',
    },
    signupHeader: {
      backgroundColor: 'black',
      color: 'white',
    },
    loginMain: {
      backgroundColor: 'black',
      color: 'white',
    },
    signupMain: {
      backgroundColor: 'white',
      color: 'black',
      border: '1px solid black',
    },
  };

  return (
    <div className="flex min-h-screen flex-col">
      <Header config={headerConfig}>
        <Link
          to="/login"
          style={{ ...buttonStyles.base, ...buttonStyles.loginHeader }}
        >
          Login
        </Link>
        <Link
          to="/signup"
          style={{ ...buttonStyles.base, ...buttonStyles.signupHeader }}
        >
          Signup
        </Link>
      </Header>

      <div className="flex-1 flex items-center justify-center bg-background">
        <div className="container mx-auto px-4 md:px-6">
          <div className="space-y-6 text-center">
            <h1 className="text-4xl font-bold tracking-tighter sm:text-5xl md:text-6xl">DalExchange</h1>
            <p className="text-lg text-muted-foreground">
              Welcome to DalExchange! Discover pre-loved treasures and give them a new home at our thrift store.
            </p>
            <div className="flex flex-col gap-2 sm:flex-row sm:justify-center">
              <Link
                to="/login"
                style={{ ...buttonStyles.base, ...buttonStyles.loginMain }}
              >
                Login
              </Link>
              <Link
                to="/signup"
                style={{ ...buttonStyles.base, ...buttonStyles.signupMain }}
              >
                Signup
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LandingPage;
