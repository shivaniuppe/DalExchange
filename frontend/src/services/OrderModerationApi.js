import AxiosInstance from "./AxiosInstance";

const OrderModerationApi = {
  fetchOrders: async () => {
    try {
      const response = await AxiosInstance.get("/admin/orders/all");
      return response.data;
    } catch (error) {
      throw new Error("Failed to fetch orders");
    }
  },

  fetchOrderDetails: async (orderId) => {
    try {
      const response = await AxiosInstance.get(`/admin/orders/orderDetails/${orderId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch order details for order ID: ${orderId}`);
    }
  },

  updateOrder: async (orderId, updatedOrder) => {
    try {
      await AxiosInstance.put(`/admin/orders/update/${orderId}`, updatedOrder);
    } catch (error) {
      throw new Error("Failed to update the order");
    }
  },

  refundOrder: async (orderId, amount) => {
    try {
      await AxiosInstance.put(`/admin/orders/refund/${orderId}`, amount, {
        headers: {
          'Content-Type': 'text/plain'
        }
      });
    } catch (error) {
      throw new Error("Failed to process the refund");
    }
  }
};

export default OrderModerationApi;
