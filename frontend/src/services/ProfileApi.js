import AxiosInstance from "./AxiosInstance";

export const ProfileApi = {
  getProfile: async (userId, setters, params) => {
    try {
      setters.isLoading(true);
      setters.error(null);
      const response = await AxiosInstance.get("/profile", {
        params: params,
        paramsSerializer: { indexes: null }
      });
      setters.profileData(response.data);
    } catch (error) {
      console.error("Error fetching profile details:", error);
      setters.error(error);
    } finally {
      setters.isLoading(false);
    }
  }
};
