import React, { useState, useEffect } from "react";
import Header from "../../components/Header";
import { useNavigate } from "react-router-dom";
import SubHeader from "../../components/SubHeader";
import Loader from "../../components/Loader";
import ErrorAlert from "../../components/ErrorAlert";
import { ProfileApi } from "../../services/ProfileApi";
import { useUser } from "../../context/UserContext";
import UserPlaceholder from "../../assets/images/placeholder-user.jpg";

export default function Profile() {
  const navigate = useNavigate();
  const userId = 1;
  const { user } = useUser();

  const [profileData, setProfileData] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const headerConfig = {
    search: false,
    requests: true,
    notifications: true,
    add: true,
    profile: true
  };

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    ProfileApi.getProfile(userId, {
      isLoading: setIsLoading,
      error: setError,
      profileData: setProfileData
    }, params);
  }, [userId]);

  return (
    <>
      <div className="bg-gray-100 pb-4 h-screen max-h-100">
        <Header config={headerConfig} />
        <SubHeader title={'User Profile'} backPath={'/products'} />
        {isLoading && <Loader title={'Loading Profile Details...'} />}
        {!isLoading && error && <ErrorAlert message={error.message} />}
        {!isLoading && !error && profileData && (
          <div className="container mx-auto px-4 md:px-6 overflow-hidden py-">
            <div className="flex flex-col items-center gap-4">
              <div className="pt-6">
              <img
              alt=''
              className='w-20 h-20 rounded-full'
              src={user?.profilePicture ? user.profilePicture : UserPlaceholder}
              onClick={() => navigate('/profile')}
              />  
              </div>
              <div className="text-center">
                <h2 className="text-xl font-bold">{profileData?.username}</h2>
                <p className="text-gray-500 text-sm pt-4">
                  {profileData?.bio}
                </p>
                <br />
                <button
                  type="button"
                  className="text-white bg-black border border-gray-300 focus:outline-none hover:bg-white hover:text-black focus:ring-4 focus:ring-gray-100 font-medium rounded-lg text-sm px-5 py-2.5 my-2"
                  onClick={() => {
                    navigate("edit-profile");
                  }}
                >
                  Edit Profile
                </button>
              </div>
            </div>
            <div className="space-y-8">
              <div>
                <div className=" grid gap-10 grid-cols-2 grid-rows-2 mx-50 content-center py-8">
                  <div
                    className="flex justify-end"
                    onClick={() => {
                      navigate("saved-items");
                    }}
                  >
                    <div className="bg-gray-200 text-center p-5 rounded-md w-80 cursor-pointer">
                      <p className="text-base">Saved Items</p>
                    </div>
                  </div>
                  <div
                    className="flex justify-start"
                    onClick={() => {
                      navigate("purchase-history");
                    }}
                  >
                    <div className="bg-gray-200 text-center p-5 rounded-md w-80 cursor-pointer">
                      <p className="text-base">Purchase History</p>
                    </div>
                  </div>
                  <div
                    className="flex justify-end"
                    onClick={() => {
                      navigate("sold-items");
                    }}
                  >
                    <div className="bg-gray-200 text-center p-5 rounded-md w-80 cursor-pointer">
                      <p className="text-base">Sold Items</p>
                    </div>
                  </div>
                  <div className="flex justify-start"
                    onClick={() => {
                      navigate("reviews");
                    }}
                  >
                    <div className="bg-gray-200 text-center p-5 rounded-md w-80 cursor-pointer">
                      <p className="text-base">Reviews</p>
                    </div>
                  </div>
                </div>
              </div>         
            </div>
          </div>
        )}
      </div>
    </>
  );
}
