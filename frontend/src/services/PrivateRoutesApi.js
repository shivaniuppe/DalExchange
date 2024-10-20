import AxiosInstance from "./AxiosInstance";

const PrivateRoutesApi = {
  currentUser: async (token) => {
    try {
      return await AxiosInstance.get('/auth/current-user', {
        headers: { 'Authorization': `Bearer ${token}` }
      });
    } catch (error) {
      throw new Error(error);
    }
  },
};

export default PrivateRoutesApi;
