import AxiosInstance from "./AxiosInstance";

const ProductDetailsApi = {
  fetchProductDetails: async (productId) => {
    try {
      const response = await AxiosInstance.get("/product_details", {
        params: {
          productId: productId
        }
      });
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch product details');
    }
  },

  addToFavorite: async (productId) => {
    try {
      const response = await AxiosInstance.get("/product_details/favorite", {
        params: {
          productId: productId
        }
      });
      return response.data;
    } catch (error) {
      throw new Error('Failed to add to favorites');
    }
  },

  createTradeRequest: async (requestBody) => {
    try {
      const response = await AxiosInstance.post("/create_trade_request", requestBody);
      return response.data; // Adjust as per API response format
    } catch (error) {
      throw new Error('Failed to create trade request');
    }
  }
  
};

export default ProductDetailsApi;
