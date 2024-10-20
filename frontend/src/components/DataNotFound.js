import React from "react";
import { useNavigate } from "react-router-dom";

const DataNotFound = ({ message }) => {
  const navigate = useNavigate();

  return (
    <div className="flex flex-row items-center justify-center bg-gray-100 text-center">
      <div className="bg-white p-8 rounded-lg shadow-lg">
        <h1 className="text-4xl font-bold text-gray-800 mb-4">
          Data Not Found
        </h1>
        <p className="text-lg text-gray-600 mb-6">{message}</p>
        <button
          className="text-white bg-black border border-gray-300 focus:outline-none hover:bg-white hover:text-black focus:ring-4 focus:ring-gray-100 font-medium rounded-lg text-sm px-5 py-2.5 my-2"
          onClick={() => {
            navigate("/profile");
          }}
        >
          Go to Profile
        </button>
      </div>
    </div>
  );
};

export default DataNotFound;
