import AxiosInstance from "./AxiosInstance";

const FeedbackModerationApi = {
  fetchReviews: async () => {
    try {
      const response = await AxiosInstance.get("/admin/reviews/all");
      return response.data;
    } catch (error) {
      throw new Error("Failed to fetch reviews");
    }
  },

  fetchProductById: async (productId) => {
    try {
      const response = await AxiosInstance.get(`/admin/products/${productId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch product with ID: ${productId}`);
    }
  },

  fetchUserById: async (userId) => {
    try {
      const response = await AxiosInstance.get(`/admin/users/${userId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch user with ID: ${userId}`);
    }
  },

  deleteReview: async (productId, userId) => {
    try {
      await AxiosInstance.delete("/admin/reviews/delete", { params: { productId, userId } });
    } catch (error) {
      throw new Error("Failed to delete the review");
    }
  }
};

export default FeedbackModerationApi;
