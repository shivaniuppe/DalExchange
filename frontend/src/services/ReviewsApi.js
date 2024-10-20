import AxiosInstance from "./AxiosInstance";

export const ReviewsApi = {
  fetchProductRatings: async (productId) => {
    try {
      const response = await AxiosInstance.get("/profile/product_ratings", {
        params: { id: productId },
        paramsSerializer: { indexes: null },
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  },
};
