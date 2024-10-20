import AxiosInstance from "./AxiosInstance";

export const ProductListingAPI = {
  get: async (setters, params) => {
    try {
      setters.isLoading(true);
      setters.error(null);
      const response = await AxiosInstance.get("/products_listing", { 
          params: params,
          paramsSerializer: {indexes: null }
      });
      const { content, ...pageData } = response.data;
      setters.products(content);
      setters.pageData(pageData);
    } catch (error) {
      console.error("Error fetching products:", error);
      setters.error(error);
    } finally {
      setters.isLoading(false);
    }
  },
  getCategories: async (setters) => {
    try {
      const response = await AxiosInstance.get("/product_categories");
      setters.categories(response.data);
    } catch (error) {
      console.error("Error fetching products:", error);
    }
  }
};
