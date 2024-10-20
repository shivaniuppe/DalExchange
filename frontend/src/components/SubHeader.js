import React from 'react';
import { Link } from 'react-router-dom';

const SubHeader = ({ title, backPath }) => {
  const path = window.location.pathname;

  const handleLogout = () => {
    // Add your logout logic here
    console.log("Logged out");
    // For example, clearing localStorage and redirecting to the login page
    localStorage.clear();
    window.location.href = "/login";
  };

  return (
    <div className="bg-gray-100 p-4 border-b border-gray-300">
      <div className="flex items-center justify-between">
        <div className="flex items-center">
          <Link to={backPath} className="flex items-center">
            <button className="mr-4 text-gray-600 hover:text-gray-800">
              <svg
                className="h-6 w-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M15 19l-7-7 7-7"
                ></path>
              </svg>
            </button>
          </Link>
          <h2 className="text-xl font-medium text-black">{title}</h2>
        </div>
        {path === "/profile" && (
          <button
            onClick={handleLogout}
            className="bg-red-500 text-white px-8 py-2 rounded hover:bg-red-600"
          >
            Logout
          </button>
        )}
      </div>
    </div>
  );
};

export default SubHeader;
