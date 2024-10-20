import AxiosInstance from "./AxiosInstance";

const fetchSavedItems = async () => {
  try {
    const response = await AxiosInstance.get("/profile/saved_products");
    return response.data;
  } catch (error) {
    throw error;
  }
};

const removeSavedItem = async (productId) => {
  try {
    const response = await AxiosInstance.put(`/profile/remove_saved/${productId}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

const SavedItemsApi = {
  fetchSavedItems,
  removeSavedItem,
};

export default SavedItemsApi;
