import AxiosInstance from "./AxiosInstance";

const UserModerationApi = {
  fetchUsers: async () => {
    try {
      const response = await AxiosInstance.get("/admin/users/all");
      return response.data;
    } catch (error) {
      throw new Error("Failed to fetch users");
    }
  },

  fetchUserDetails: async (userId) => {
    try {
      const response = await AxiosInstance.get(`/admin/users/${userId}`);
      return response.data;
    } catch (error) {
      throw new Error("Failed to fetch user details");
    }
  },

  updateUser: async (userId, updatedUser) => {
    try {
      await AxiosInstance.put(`/admin/users/${userId}`, updatedUser);
    } catch (error) {
      throw new Error("Failed to update user");
    }
  },

  resetPassword: async (email) => {
    try {
      const response = await AxiosInstance.post(
        '/auth/forgot-password',
        `email=${encodeURIComponent(email)}`,
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
        }
      );
      return response.data;
    } catch (error) {
      throw new Error("Failed to request password reset");
    }
  },

  toggleUserStatus: async (userId, active) => {
    try {
      const updatedUser = { active };
      await AxiosInstance.put(`/admin/users/${userId}`, updatedUser);
    } catch (error) {
      throw new Error("Failed to toggle user status");
    }
  },

  deleteUser: async (userId) => {
    try {
      await AxiosInstance.delete(`/admin/users/${userId}`);
    } catch (error) {
      throw new Error("Failed to delete user");
    }
  }
};

export default UserModerationApi;
