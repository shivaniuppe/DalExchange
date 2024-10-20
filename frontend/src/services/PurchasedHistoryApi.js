import AxiosInstance from "./AxiosInstance";

const PurchasedHistoryApi = async (productId) => {
  try {
    const response = await AxiosInstance.get("/profile/purchased_products", {
      params: { id: productId },
      paramsSerializer: { indexes: null },
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

export default PurchasedHistoryApi;
