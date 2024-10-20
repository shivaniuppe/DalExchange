import AxiosInstance from "./AxiosInstance";

export const NotificationApi = {
  get: async (setters) => {
    try {
      setters.isLoading(true);
      setters.error(null);
      const response = await AxiosInstance.get("/notifications");
      setters.notifications(response.data);
    } catch (error) {
      console.error("Error fetching trade requests: ", error);
      setters.error(error);
    } finally {
      setters.isLoading(false);
    }
  },
  mark: async (id) => {
    try {
      await AxiosInstance.put("/notifications/mark/"+id, null);
    } catch (error) {
      console.error("Error marking notifcation as read: ", error);
    }
  },
  create: async (body) => {
    try {
      const response = await AxiosInstance.post("notifications/add", body);
      console.log("Notifcation created successfully:", response.data);
    } catch (error) {
      console.error("Error creating notifcation:", error);
    }
  }
};