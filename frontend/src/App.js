import { Route, Routes } from 'react-router-dom';
import './App.css';
import ProductList from './features/product_list/ProductList';
import TradeRequests from './features/trade_requests/TradeRequests';
import ProductDetails from './features/product_details/ProductDetails';
import Login from './features/authentication/Login';
import Signup from './features/authentication/Signup';
import VerifyEmail from './features/authentication/VerifyEmail';
import LandingPage from './features/authentication/LandingPage';
import ForgotPassword from './features/authentication/ForgotPassword';
import ResetPassword from './features/authentication/ResetPassword';
import Notification from './features/notification/Notification';
import AddProduct from './features/add_product/AddProduct';
import ProfilePage from './features/profile_page/Profile';
import SoldItems from "./features/profile_page/SoldItems";
import SavedItems from "./features/profile_page/SavedItems";
import EditProfile from "./features/profile_page/EditProfile";
import Reviews from './features/profile_page/Reviews';
import PurchaseHistory from './features/profile_page/PurchaseHistory';
import { SearchFilterProvider } from './context/SearchFilterContext';
import UnauthenticatedRoute from './features/private_routes/UnauthenticatedRoute';
import StudentPrivateRoute from './features/private_routes/StudentPrivateRoute';
import AdminPrivateRoute from './features/private_routes/AdminPrivateRoute';
import NotAuthorizedPage from './features/authentication/NotAuthorizedPage';
import Layout from './components/AdminLayout';
import AdminDashboard from "./features/admin_moderation/AdminDashboard";
import OrderModeration from './features/admin_moderation/OrderModeration';
import UserModeration from "./features/admin_moderation/UserModeration";
import ProductModeration from "./features/admin_moderation/ProductModeration";
import FeedbackModeration from "./features/admin_moderation/FeedbackModeration";
import PaymentCancelled from './features/trade_requests/components/CancelPayment';
import SuccessPage from './features/trade_requests/components/PaymentSuccess';


function App() {
  return (
      <div className="App">
        <Routes>
          <Route path="/" element={<UnauthenticatedRoute> <LandingPage /> </UnauthenticatedRoute>} />

          <Route path="/login" element={<UnauthenticatedRoute> <Login /> </UnauthenticatedRoute>} />
          <Route path="/signup" element={<UnauthenticatedRoute> <Signup /> </UnauthenticatedRoute>} />
          <Route path="/forgot-password" element={<UnauthenticatedRoute> <ForgotPassword /> </UnauthenticatedRoute>} />
          <Route path="/verify-email" element={<UnauthenticatedRoute> <VerifyEmail /> </UnauthenticatedRoute>} />
          <Route path="/reset-password" element={<UnauthenticatedRoute> <ResetPassword /> </UnauthenticatedRoute>} />
          <Route path="/not-authorized" element={<NotAuthorizedPage />} />

          <Route path="/products" element={
            <StudentPrivateRoute>
              <SearchFilterProvider>
                <ProductList />
              </SearchFilterProvider>
            </StudentPrivateRoute>
          } />
          <Route path="/products/add-product" element={<StudentPrivateRoute><AddProduct /> </StudentPrivateRoute>}/>
          <Route path="/products/:productId" element={<StudentPrivateRoute> <ProductDetails/> </StudentPrivateRoute> } />

          <Route path="/trade_requests" element={<StudentPrivateRoute> <TradeRequests/> </StudentPrivateRoute> } />

          <Route path="/admin-moderation/dashboard" element={<AdminPrivateRoute><Layout> <AdminDashboard /> </Layout></AdminPrivateRoute>} />
          <Route path="/admin-moderation/orders" element={<AdminPrivateRoute><Layout> <OrderModeration /> </Layout></AdminPrivateRoute>} />
          <Route path="/admin-moderation/users" element={<AdminPrivateRoute><Layout><UserModeration /></Layout></AdminPrivateRoute>} />
          <Route path="/admin-moderation/products" element={<AdminPrivateRoute><Layout><ProductModeration /></Layout></AdminPrivateRoute>} />
          <Route path="/admin-moderation/feedback" element={<AdminPrivateRoute><Layout> <FeedbackModeration /> </Layout></AdminPrivateRoute>} />

          <Route path="/profile/purchase-history" element={<StudentPrivateRoute> <PurchaseHistory /> </StudentPrivateRoute>} />
          <Route path="/profile" element={<StudentPrivateRoute> <ProfilePage /> </StudentPrivateRoute> } />
          <Route path="/profile/sold-items" element={<StudentPrivateRoute> <SoldItems /> </StudentPrivateRoute>} />
          <Route path="/profile/saved-items" element={<StudentPrivateRoute> <SavedItems /> </StudentPrivateRoute>} />
          <Route path="/profile/edit-profile" element={<StudentPrivateRoute> <EditProfile /> </StudentPrivateRoute>} />
          <Route path="/profile/reviews" element={<StudentPrivateRoute> <Reviews /> </StudentPrivateRoute>} />

          <Route path="/notifications" element={<StudentPrivateRoute> <Notification/> </StudentPrivateRoute>} />
          
          <Route path="/payment/success" element={ <SuccessPage /> } />
          <Route path="/payment/fail" element={ <PaymentCancelled /> } />


        </Routes>
      </div>
  );
}

export default App;



