import AxiosInstance from './AxiosInstance';

const AdminDashboardApi = {
  fetchDashboardData: async () => {
    try {
      const response = await AxiosInstance.get('admin/stats');
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch dashboard data');
    }
  },

  fetchItemsSold: async () => {
    try {
      const response = await AxiosInstance.get('admin/items-sold');
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch items sold data');
    }
  },

  fetchTopSellingCategories: async () => {
    try {
      const response = await AxiosInstance.get('admin/top-selling-categories');
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch top selling categories');
    }
  },

  fetchBestSellingProducts: async () => {
    try {
      const response = await AxiosInstance.get('admin/best-selling-products');
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch best selling products');
    }
  }
};

export default AdminDashboardApi;
