
import AxiosInstance from "./AxiosInstance";
//import axios from 'axios';
//import { loadStripe } from "@stripe/stripe-js";

export const TradeRequestApi = {
  getTradeRequests: async (setters) => {
    try {
      setters.isLoading(true);
      setters.error(null);
      const sellResponse = await AxiosInstance.get("/sell_requests");
      const buyResponse = await AxiosInstance.get("/buy_requests");
      setters.sellRequests(sellResponse.data);
      setters.buyRequests(buyResponse.data);
    } catch (error) {
      console.error("Error fetching trade requests: ", error);
      setters.error(error);
    } finally {
      setters.isLoading(false);
    }
  },
  updateStatus: async (id, status) => {
    try {
      await AxiosInstance.put("/update_trade_status/" + id, null, {
        params: {
          status: status
        }
      });
    } catch (error) {
      console.error("Error updating trade request status: ", error);
      alert("Error updating trade request status!");
    }
  },
  create: async (body) => {
    try {
      const response = await AxiosInstance.post("/create_trade_request", body);
      console.log("Trade request created successfully:", response.data);
    } catch (error) {
      console.error("Error creating trade request:", error);
    }
  },
  createPaymentIntent: async (productId, orderId) => {
    try {
      const response = await AxiosInstance.post("/payment/create_payment_intent", { productId, orderId});
      const { sessionId } = response.data;
      return sessionId;
    } catch (error) {
      console.error('Error creating payment intent:', error);
      throw error;
    }
  },
  saveShippingAddress: async (body) => {
    try {
      const response = await AxiosInstance.post("/payment/save_order_details", body);
      console.log("Address saved succefully:", response.data);
      if(response.status === 200 || response.status === 201) {
        return response.data;
      }
      return null;
    } catch (error) {
      console.error("Error saving shipping address:", error);
      return null;
    }
  },
  savePayment: async (body) => {
    try {
      const response = await AxiosInstance.post(`payment/save_payment`, body);
      console.log("Payment added successfully:", response.data);
      if(response.status === 200) {
        return true;
      }
      return false;
    } catch (error) {
      console.error("Error creating trade request:", error);
      return false;
    }
  },
  paymentSuccess: async (body) => {
    try {
      const response = await AxiosInstance.put(`payment/payment_success`, {productId: body.productId});
      console.log("Payment added successfully:", response.data);
      if(response.status === 200) {
        return true;
      }
      return false;
    } catch (error) {
      console.error("Error creating trade request:", error);
      return false;
    }
  },
  saveRating: async (body) => {
    try {
      const response = await AxiosInstance.post(`payment/save_rating`, body);
      console.log("Payment added successfully:", response.data);
      if(response.status === 200) {
        return true;
      }
      return false;
    } catch (error) {
      console.error("Error creating trade request:", error);
      return false;
    }
  },
};
