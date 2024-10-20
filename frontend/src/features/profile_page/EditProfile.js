import React, { useState, useEffect } from "react";
import EditProfileApi from "../../services/EditProfileApi";
import Loader from "../../components/Loader";
import ErrorAlert from "../../components/ErrorAlert";
import DataNotFound from "../../components/DataNotFound";
import toast from 'react-hot-toast';

import Header from "../../components/Header";
import SubHeader from "../../components/SubHeader";

const EditProfile = () => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
    phoneNo: "",
    fullName: "",
    profilePicture: "",
    bio: "",
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const [profileData, setProfileData] = useState([]);
  const headerConfig = {
    search: false,
    requests: true,
    notifications: true,
    add: true,
    profile: true,
  };

  const fetchUserProfile = async () => {
    try {
      setIsLoading(true);
      const data = await EditProfileApi.fetchUserProfile();
      setProfileData(data);
      setFormData(data);
    } catch (error) {
      setError(error);
    } finally {
      setIsLoading(false);
    }
  };

  const updateUserProfile = async (payload) => {
    try {
      await EditProfileApi.updateUserProfile( payload);
      toast.success('Profile updated successfully!');
    } catch (error) {
      toast.error('Failed to update profile.');
    }
  };

  useEffect(() => {
    fetchUserProfile();
  }, []);

  const validateForm = () => {
    const newErrors = {};

    if (formData.username.trim() === "") {
      newErrors.username = "Username cannot be empty.";
    }

    if (formData.password.trim() === "") {
      newErrors.password = "Password cannot be empty.";
    }

    if (!formData.email.includes("@")) {
      newErrors.email = "Invalid email format.";
    }

    if (formData.phoneNo.length > 10) {
      newErrors.phoneNo = "Phone number cannot exceed 10 digits.";
    }

    if (formData.phoneNo.length <= 0) {
      newErrors.phoneNo = "Phone number is not valid";
    }

    if (formData.fullName.trim() === "") {
      newErrors.fullName = "Full Name cannot be empty.";
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      toast.error('Please fill out all fields correctly.');
      return;
    }

    await updateUserProfile(formData);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });

    setErrors({
      ...errors,
      [name]: "",
    });
  };

  return (
    <>
      <Header config={headerConfig} />
      <SubHeader title={'Back to Profile'} backPath={'/profile'} />
      {isLoading && <Loader title={'Loading Profile Details...'} />}
      {!isLoading && error && <ErrorAlert message={error.message} />}
      {!isLoading && !error && profileData && Object.keys(profileData).length !== 0 ? (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center pt-14">
          <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
            <h2 className="text-2xl font-bold mb-6 text-center">Edit Profile</h2>
            <form onSubmit={handleSubmit}>
              <div className="mb-4">
                <label className="block text-gray-700 mb-2">Username</label>
                <input
                  type="text"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  className={`w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 ${errors.username ? 'border-red-500' : 'focus:ring-blue-500'}`}
                />
                {errors.username && (
                  <span className="text-red-500 text-sm">{errors.username}</span>
                )}
              </div>
              <div className="mb-4">
                <label className="block text-gray-700 mb-2">Email</label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  className={`w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 ${errors.email ? 'border-red-500' : 'focus:ring-blue-500'}`}
                />
                {errors.email && (
                  <span className="text-red-500 text-sm">{errors.email}</span>
                )}
              </div>
              <div className="mb-4">
                <label className="block text-gray-700 mb-2">Phone Number</label>
                <input
                  type="tel"
                  name="phoneNo"
                  value={formData.phoneNo}
                  onChange={handleChange}
                  className={`w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 ${errors.phoneNo ? 'border-red-500' : 'focus:ring-blue-500'}`}
                />
                {errors.phoneNo && (
                  <span className="text-red-500 text-sm">{errors.phoneNo}</span>
                )}
              </div>
              <div className="mb-4">
                <label className="block text-gray-700 mb-2">Full Name</label>
                <input
                  type="text"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleChange}
                  className={`w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 ${errors.fullName ? 'border-red-500' : 'focus:ring-blue-500'}`}
                />
                {errors.fullName && (
                  <span className="text-red-500 text-sm">{errors.fullName}</span>
                )}
              </div>
              <div className="mb-4">
                <label className="block text-gray-700 mb-2">Bio</label>
                <textarea
                  name="bio"
                  value={formData.bio}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <button
                type="submit"
                className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition duration-200"
              >
                Save Changes
              </button>
            </form>
          </div>
        </div>
      ) : (
        <div className="my-20">
          <DataNotFound message={"Oops! No items sold yet."} />
        </div>
      )}
    </>
  );
};

export default EditProfile;
