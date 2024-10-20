import { useState, useEffect } from "react";
import OrderModerationApi from "../../services/OrderModerationApi";

export default function OrderModeration() {
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [orderStatus, setOrderStatus] = useState("");
  const [shippingAddress, setShippingAddress] = useState({
    billingName: "",
    country: "",
    line1: "",
    line2: "",
    city: "",
    state: "",
    postalCode: ""
  });
  const [adminComments, setAdminComments] = useState("");
  const [message, setMessage] = useState("");
  const [showMessage, setShowMessage] = useState(false);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const data = await OrderModerationApi.fetchOrders();
        setOrders(data);
      } catch (error) {
        console.error("There was an error fetching the orders!", error);
      }
    };

    fetchOrders();
  }, []);

  const handleOrderClick = async (orderId) => {
    try {
      const orderData = await OrderModerationApi.fetchOrderDetails(orderId);
      setSelectedOrder(orderData);
      setOrderStatus(orderData.orderStatus);
      setShippingAddress(orderData.shippingAddress || {
        billingName: "",
        country: "",
        line1: "",
        line2: "",
        city: "",
        state: "",
        postalCode: ""
      });
      setAdminComments(orderData.adminComments || "");
    } catch (error) {
      console.error("There was an error fetching the order details!", error);
    }
  };

  const handleSaveChanges = async () => {
    try {
      const updatedOrder = {
        shippingAddress,
        orderStatus,
        adminComments
      };
      await OrderModerationApi.updateOrder(selectedOrder.orderId, updatedOrder);
      setOrders(orders.map((order) => (order.orderId === selectedOrder.orderId ? { ...order, ...updatedOrder } : order)));
      setSelectedOrder(null);
      showMessageWithFade("Order and shipping address updated successfully");
    } catch (error) {
      showMessageWithFade("There was an error updating the order or shipping address.");
    }
  };

  const handleRefundOrder = async () => {
    if (selectedOrder.totalAmount === 0) {
      showMessageWithFade("Order has already been refunded.");
      return;
    }

    try {
      await OrderModerationApi.refundOrder(selectedOrder.orderId, selectedOrder.totalAmount.toString());
      setOrders(orders.map((order) => (order.orderId === selectedOrder.orderId ? { ...order, totalAmount: 0 } : order)));
      showMessageWithFade("Order refunded successfully");
      setSelectedOrder(null);
    } catch (error) {
      showMessageWithFade("There was an error processing the refund.");
    }
  };

  const showMessageWithFade = (msg) => {
    setMessage(msg);
    setShowMessage(true);
    setTimeout(() => {
      setShowMessage(false);
    }, 5000);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setShippingAddress({
      ...shippingAddress,
      [name]: value
    });
  };

  const formatDate = (dateArray) => {
    if (Array.isArray(dateArray) && dateArray.length >= 3) {
      const [year, month, day] = dateArray;
      const date = new Date(year, month - 1, day);
      return date.toLocaleDateString();
    }
    return "Invalid Date";
  };

  return (
    <div className="flex flex-col min-h-screen w-full">
      <header className="flex h-14 lg:h-[60px] items-center gap-4 border-b bg-gray-200 px-6 mx-4 mt-4 rounded-lg">
        <div className="w-full flex justify-center">
          <h1 className="text-3xl font-bold">Order Moderation</h1>
        </div>
      </header>
        <main className="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-6">
          {showMessage && (
            <div className="fixed top-0 left-0 right-0 bg-black text-white p-4 z-50 flex justify-between items-center">
              <span>{message}</span>
              <button
                className="text-white bg-transparent border border-white rounded px-2 py-1"
                onClick={() => setShowMessage(false)}
              >
                Close
              </button>
            </div>
          )}
          {selectedOrder ? (
            <div>
              <div className="flex items-center justify-between mb-6">
                <h2 className="text-2xl font-bold">Order #{selectedOrder.orderId}</h2>
                <button className="px-4 py-2 bg-gray-500 text-white rounded" onClick={() => setSelectedOrder(null)}>
                  Back to Orders
                </button>
              </div>
              <div className="grid md:grid-cols-2 gap-6">
                <div className="border border-gray-300 rounded-lg bg-white text-black p-6">
                  <div className="mb-4">
                    <h2 className="text-lg font-bold text-black border-b pb-2">View Order Details</h2>
                  </div>
                  <div className="grid gap-4">
                    <div className="grid gap-1">
                      <div className="text-black font-bold">Customer</div>
                      <div className="font-normal">{selectedOrder.fullName}</div>
                    </div>
                    <div className="grid gap-1">
                      <div className="text-black font-bold">Product</div>
                      <div className="font-normal">{selectedOrder.productTitle}</div>
                    </div>
                    <div className="grid gap-1">
                      <div className="text-black font-bold">Order Date</div>
                      <div className="font-normal">{formatDate(selectedOrder.transactionDatetime)}</div>
                    </div>
                    <div className="grid gap-1">
                      <div className="text-black font-bold">Order Total</div>
                      <div className="font-normal">${selectedOrder.totalAmount.toFixed(2)}</div>
                    </div>
                    <div className="grid gap-1">
                      <div className="text-black font-bold">Shipping Address</div>
                      <address className="not-italic font-normal">
                        {`${shippingAddress.billingName}, ${shippingAddress.line1}, ${shippingAddress.line2}, ${shippingAddress.city}, ${shippingAddress.state}, ${shippingAddress.country}, ${shippingAddress.postalCode}`}
                      </address>
                    </div>
                    <div className="grid gap-1">
                      <div className="text-black font-bold">Admin Comments</div>
                      <div className="font-normal">{adminComments}</div>
                    </div>
                  </div>
                </div>
                <div className="border border-gray-300 rounded-lg bg-white text-black p-6">
                  <div className="mb-4">
                    <h2 className="text-lg font-bold text-black border-b pb-2">Edit Order Details</h2>
                  </div>
                  <form className="grid gap-4">
                    <div className="grid gap-2">
                      <label htmlFor="order-status" className="font-bold">Order Status</label>
                      <select
                        id="order-status"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={orderStatus}
                        onChange={(e) => setOrderStatus(e.target.value)}
                      >
                        <option value="Pending">Pending</option>
                        <option value="Shipped">Shipped</option>
                        <option value="Delivered">Delivered</option>
                        <option value="Cancelled">Cancelled</option>
                      </select>
                    </div>
                    <div className="grid gap-2">
                      <label htmlFor="billingName" className="font-bold">Billing Name</label>
                      <input
                        id="billingName"
                        name="billingName"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={shippingAddress.billingName}
                        onChange={handleInputChange}
                      />
                    </div>
                    <div className="grid gap-2">
                      <label htmlFor="country" className="font-bold">Country</label>
                      <input
                        id="country"
                        name="country"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={shippingAddress.country}
                        onChange={handleInputChange}
                      />
                    </div>
                    <div className="grid gap-2">
                      <label htmlFor="line1" className="font-bold">Address Line 1</label>
                      <input
                        id="line1"
                        name="line1"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={shippingAddress.line1}
                        onChange={handleInputChange}
                      />
                    </div>
                    <div className="grid gap-2">
                      <label htmlFor="line2" className="font-bold">Address Line 2</label>
                      <input
                        id="line2"
                        name="line2"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={shippingAddress.line2}
                        onChange={handleInputChange}
                      />
                    </div>
                    <div className="grid gap-2">
                      <label htmlFor="city" className="font-bold">City</label>
                      <input
                        id="city"
                        name="city"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={shippingAddress.city}
                        onChange={handleInputChange}
                      />
                    </div>
                    <div className="grid gap-2">
                      <label htmlFor="state" className="font-bold">State</label>
                      <input
                        id="state"
                        name="state"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={shippingAddress.state}
                        onChange={handleInputChange}
                      />
                    </div>
                    <div className="grid gap-2">
                      <label htmlFor="postalCode" className="font-bold">Postal Code</label>
                      <input
                        id="postalCode"
                        name="postalCode"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        value={shippingAddress.postalCode}
                        onChange={handleInputChange}
                      />
                    </div>
                    <div className="grid gap-2">
                      <label htmlFor="admin-comments" className="font-bold">Admin Comments</label>
                      <textarea
                        id="admin-comments"
                        className="w-full p-2 border border-gray-300 rounded-lg"
                        rows={3}
                        value={adminComments}
                        onChange={(e) => setAdminComments(e.target.value)}
                        placeholder="Add any admin comments here..."
                      />
                    </div>
                    <div className="flex gap-2">
                      <button
                        type="button"
                        className="px-4 py-2 bg-transparent text-black border border-black rounded hover:bg-black hover:text-white transition"
                        onClick={handleRefundOrder}
                      >
                        Refund Order
                      </button>
                      <button
                        type="button"
                        className="px-4 py-2 bg-black text-white rounded hover:bg-gray-700 transition"
                        onClick={handleSaveChanges}
                      >
                        Save Changes
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          ) : (
            <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
              {orders.map((order) => (
                <div
                  key={order.orderId}
                  onClick={() => handleOrderClick(order.orderId)}
                  className="cursor-pointer border border-gray-300 rounded-lg p-6 bg-white text-black hover:shadow-lg transition-shadow"
                >
                  <div className="mb-4">
                    <h2 className="text-lg font-bold text-black">Order #{order.orderId}</h2>
                    <p className="text-gray-500">{order.fullName} - {formatDate(order.transactionDatetime)}</p>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="text-gray-500">
                      {order.productTitle} - {order.orderStatus}
                    </div>
                    <div className="font-medium">${order.totalAmount.toFixed(2)}</div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </main>
    </div>
  );
}
