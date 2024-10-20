import AxiosInstance from "./AxiosInstance";

const fetchSoldItems = async (productId) => {
  try {
    const response = await AxiosInstance.get("/profile/sold_products", {
      params: { id: productId },
      paramsSerializer: { indexes: null },
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

const SoldProductsApi = {
  fetchSoldItems,
};

export default SoldProductsApi;
