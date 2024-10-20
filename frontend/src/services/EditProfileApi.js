import AxiosInstance from "./AxiosInstance";

const EditProfileApi = {
  fetchUserProfile: async () => {
    try {
      const response = await AxiosInstance.get("/profile");
      return response.data;
    } catch (error) {
      throw new Error("Failed to fetch user profile");
    }
  },

  updateUserProfile: async (payload) => {
    try {
      const response = await AxiosInstance.put("/profile/edit_user", payload, {
        headers: {
          'Content-Type': 'application/json',
        },
      });
      return response.data;
    } catch (error) {
      throw new Error("Failed to update user profile");
    }
  },
};

export default EditProfileApi;