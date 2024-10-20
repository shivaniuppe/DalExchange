import React from "react";
import { Link, useLocation } from "react-router-dom";
import StoreIcon from "../assets/icons/store-solid.svg";

function Sidebar() {
  const location = useLocation();

  const handleLogout = () => {
    localStorage.clear();
    window.location.href = "/login";
  };

  return (
    <aside className="fixed inset-y-0 left-0 flex flex-col w-64 bg-gray-200 p-6 border-r">
      <div className="flex items-center justify-center mb-8">
        <img src={StoreIcon} alt="Store Icon" className="h-10 w-10 mr-3" style={{ filter: 'invert(100%)' }} />
        <span className="text-lg font-bold text-black">Dal Exchange</span>
      </div>
      <nav className="flex flex-col space-y-4">
        <Link to="/admin-moderation/dashboard">
          <button
            className={`text-white bg-black hover:bg-gray-800 focus:outline-none font-large font-semibold rounded-lg text-sm py-2 w-full tracking-wide ${
              location.pathname === "/admin-moderation/dashboard"
                ? "bg-black"
                : "bg-black hover:bg-gray-800 transition"
            }`}
          >
            Dashboard
          </button>
        </Link>
        <Link to="/admin-moderation/orders">
          <button
            className={`text-white bg-black hover:bg-gray-800 focus:outline-none font-large font-semibold rounded-lg text-sm py-2 w-full tracking-wide ${
              location.pathname === "/admin-moderation/orders"
                ? "bg-black"
                : "bg-black hover:bg-gray-800 transition"
            }`}
          >
            Orders
          </button>
        </Link>
        <Link to="/admin-moderation/users">
          <button
            className={`text-white bg-black hover:bg-gray-800 focus:outline-none font-large font-semibold rounded-lg text-sm py-2 w-full tracking-wide ${
              location.pathname === "/admin-moderation/users"
                ? "bg-black"
                : "bg-black hover:bg-gray-800 transition"
            }`}
          >
            Users
          </button>
        </Link>
        <Link to="/admin-moderation/products">
          <button
            className={`text-white bg-black hover:bg-gray-800 focus:outline-none font-large font-semibold rounded-lg text-sm py-2 w-full tracking-wide ${
              location.pathname === "/admin-moderation/products"
                ? "bg-black"
                : "bg-black hover:bg-gray-800 transition"
            }`}
          >
            Products
          </button>
        </Link>
        <Link to="/admin-moderation/feedback">
          <button
            className={`text-white bg-black hover:bg-gray-800 focus:outline-none font-large font-semibold rounded-lg text-sm py-2 w-full tracking-wide ${
              location.pathname === "/admin-moderation/feedback"
                ? "bg-black"
                : "bg-black hover:bg-gray-800 transition"
            }`}
          >
            Feedback
          </button>
        </Link>
      </nav>
      <div className="mt-auto">
        <Link to="/logout">
          <button 
          onClick={handleLogout}
          className="text-white bg-red-600 hover:bg-red-700 focus:outline-none font-large font-semibold rounded-lg text-sm py-2 w-full tracking-wide">
            Logout
          </button>
        </Link>
      </div>
    </aside>
  );
}

export default Sidebar;
