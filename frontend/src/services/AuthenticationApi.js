import AxiosInstance from "./AxiosInstance";

const AuthenticationApi = {
  signup: async (formData) => {
    try {
      await AxiosInstance.post('/auth/signup', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
    } catch (error) {
      throw new Error(error);
    }
  },
  login: async (body) => {
    try {
      return await AxiosInstance.post('/auth/login', body);
    } catch (error) {
      throw new Error(error);
    }
  },
  currentUser: async (token) => {
    try {
      return await AxiosInstance.get('/auth/current-user', {
        headers: { 'Authorization': `Bearer ${token}` }
      });
    } catch (error) {
      throw new Error(error);
    }
  },
  verify: async (body) => {
    try {
      await AxiosInstance.post('/auth/verify', body);
    } catch (error) {
      throw new Error(error);
    }
  },
  forgotPassword: async (email) => {
    try {
      return await AxiosInstance.post('/auth/forgot-password', null, {
        params: { email: email }
      });
    } catch (error) {
      throw new Error(error);
    }
  },
  resetPassword: async (params) => {
    try {
      return await AxiosInstance.post('/auth/reset-password',
        new URLSearchParams(params), {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      });
    } catch (error) {
      throw new Error(error);
    }
  },
};

export default AuthenticationApi;