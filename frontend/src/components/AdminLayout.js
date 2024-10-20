// Layout.jsx
import React from "react";
import Sidebar from "./AdminSidebar";

const Layout = ({ children }) => {
  return (
    <div className="flex min-h-screen">
      <Sidebar />
      <div className="flex-1 ml-64">{children}</div>
    </div>
  );
};

export default Layout;


