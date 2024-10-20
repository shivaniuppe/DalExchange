import AxiosInstance from "./AxiosInstance";

const AddProductApi = {
  add: async (formData) => {
    try {
      return await AxiosInstance.post('/product/add-product', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
    } catch (error) {
      throw new Error(error);
    }
  }
};

export default AddProductApi;