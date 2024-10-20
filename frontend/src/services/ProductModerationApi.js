import AxiosInstance from "./AxiosInstance";

const ProductModerationApi = {
  fetchProducts: async () => {
    try {
      const response = await AxiosInstance.get("/admin/products");
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch products');
    }
  },

  fetchCategories: async () => {
    try {
      const response = await AxiosInstance.get("/categories");
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch categories');
    }
  },

  fetchProductDetails: async (productId) => {
    try {
      const response = await AxiosInstance.get(`/admin/products/${productId}`);
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch product details');
    }
  },

  updateProduct: async (productId, productData) => {
    try {
      await AxiosInstance.put(`/admin/products/update/${productId}`, productData);
    } catch (error) {
      throw new Error('Failed to update product');
    }
  },

  unlistProduct: async (productId, unlisted) => {
    try {
      await AxiosInstance.put(`/admin/products/unlist/${productId}`, null, {
        params: { unlisted },
      });
    } catch (error) {
      throw new Error('Failed to unlist product');
    }
  }
};

export default ProductModerationApi;
