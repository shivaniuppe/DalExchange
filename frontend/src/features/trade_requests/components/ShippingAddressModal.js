import React, { useState } from 'react';
import { TradeRequestApi } from '../../../services/TradeRequestApi';
import { loadStripe } from "@stripe/stripe-js";

const stripePromise = loadStripe('pk_test_51Pdgl8RuVIC7U6kcnDPmtuBFtSoX83Of4rWP09F9M6LkmxUPVCRgi0eRY5aAMXsxBOhCtlVpnF6JfUSRpF7NdHAh00DKHrJPs6');

const ShippingAddressModal = ({ tradeRequest, onClose }) => {
  
  const [formData, setFormData] = useState({
    billingName: '',
    country: 'us',
    line1: '',
    line2: '',
    city: '',
    state: '',
    postalCode: ''
  });

  const handleChange = (e) => {
    const { id, value } = e.target;
    setFormData(prevState => ({
      ...prevState,
      [id]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // Include productId in the request body
      const orderId = await TradeRequestApi.saveShippingAddress({ ...formData, productId: tradeRequest.product.productId });
      if (orderId !== -1) {
        makePayment(orderId);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const makePayment = async (orderId) => {
    try {
      const sessionId = await TradeRequestApi.createPaymentIntent(tradeRequest.product.productId, orderId);
      if (window.Stripe) {
        const stripe = await stripePromise;
        if (stripe) {
          const { error } = await stripe.redirectToCheckout({ sessionId });
          if (error) {
            console.error('Stripe error:', error.message);
          }
        } else {
          console.error('Stripe failed to initialize');
        }
      } else {
        console.error('Stripe is not loaded');
      }
      onClose();
    } catch (error) {
      console.error(error);
    }
  }

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50 z-50">
      <div className="bg-white rounded-lg shadow-lg w-full max-w-md">
        <div className="p-6">
          <h2 className="text-2xl font-bold mb-4">Shipping Address</h2>
          <form className="space-y-4" onSubmit={handleSubmit}>
            <div>
              <label htmlFor="billingName" className="block text-sm font-medium text-gray-700">Full Name</label>
              <input id="billingName" type="text" value={formData.billingName} onChange={handleChange} placeholder="John Doe" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
            </div>
            <div>
              <label htmlFor="country" className="block text-sm font-medium text-gray-700">Country</label>
              <select id="country" value={formData.country} onChange={handleChange} className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm">
                <option value="us">United States</option>
                <option value="ca">Canada</option>
                <option value="uk">United Kingdom</option>
                <option value="au">Australia</option>
                <option value="de">Germany</option>
              </select>
            </div>
            <div>
              <label htmlFor="line1" className="block text-sm font-medium text-gray-700">Address Line 1</label>
              <input id="line1" type="text" value={formData.line1} onChange={handleChange} placeholder="123 Main St" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
            </div>
            <div>
              <label htmlFor="line2" className="block text-sm font-medium text-gray-700">Address Line 2</label>
              <input id="line2" type="text" value={formData.line2} onChange={handleChange} placeholder="Apartment, suite, etc." className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
            </div>
            <div className="flex gap-4">
              <div className="flex-1">
                <label htmlFor="city" className="block text-sm font-medium text-gray-700">City</label>
                <input id="city" type="text" value={formData.city} onChange={handleChange} placeholder="San Francisco" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
              </div>
              <div className="flex-1">
                <label htmlFor="state" className="block text-sm font-medium text-gray-700">State</label>
                <input id="state" type="text" value={formData.state} onChange={handleChange} placeholder="CA" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
              </div>
              <div className="flex-1">
                <label htmlFor="postalCode" className="block text-sm font-medium text-gray-700">Postal Code</label>
                <input id="postalCode" type="text" value={formData.postalCode} onChange={handleChange} placeholder="94101" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
              </div>
            </div>
            <div className="flex justify-between p-4 border-t border-gray-200">
              <button type="button" onClick={onClose} className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200">Close</button>
              <button type="submit" className="px-4 py-2 text-sm font-medium text-white bg-gray-900 rounded-md hover:bg-gray-800">Make Payment</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ShippingAddressModal;














// import React, { useState } from 'react';
// import { TradeRequestApi } from '../../../services/TradeRequestApi';
// import { loadStripe } from "@stripe/stripe-js";

// const stripePromise = loadStripe('pk_test_51Pdgl8RuVIC7U6kcnDPmtuBFtSoX83Of4rWP09F9M6LkmxUPVCRgi0eRY5aAMXsxBOhCtlVpnF6JfUSRpF7NdHAh00DKHrJPs6');

// const ShippingAddressModal = ({ tradeRequest, onClose }) => {
  
//   const [formData, setFormData] = useState({
//     billingName: '',
//     country: 'us',
//     line1: '',
//     line2: '',
//     city: '',
//     state: '',
//     postalCode: ''
//   });

//   const handleChange = (e) => {
//     const { id, value } = e.target;
//     setFormData(prevState => ({
//       ...prevState,
//       [id]: value
//     }));
//   };

//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     try {
//       const isAddressSaved = await TradeRequestApi.saveShippingAddress(formData);
//       if(isAddressSaved) {
//         makePayment();
//       }
//     } catch (error) {
//       console.error(error);
//     }
//   };

//   const makePayment = async () => {
//     try {
//       const sessionId = await TradeRequestApi.createPaymentIntent(tradeRequest.product.productId);
//       if (window.Stripe) {
//         const stripe = await stripePromise;
//         if (stripe) {
//           const { error } = await stripe.redirectToCheckout({ sessionId });
//           if (error) {
//             console.error('Stripe error:', error.message);
//           }
//         } else {
//           console.error('Stripe failed to initialize');
//         }
//       } else {
//         console.error('Stripe is not loaded');
//       }
//       onClose();
//     } catch (error) {
//       console.error(error);
//     }
//   }

//   return (
//     <div className="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50 z-50">
//       <div className="bg-white rounded-lg shadow-lg w-full max-w-md">
//         <div className="p-6">
//           <h2 className="text-2xl font-bold mb-4">Shipping Address</h2>
//           <form className="space-y-4" onSubmit={handleSubmit}>
//             <div>
//               <label htmlFor="billingName" className="block text-sm font-medium text-gray-700">Full Name</label>
//               <input id="billingName" type="text" value={formData.billingName} onChange={handleChange} placeholder="John Doe" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
//             </div>
//             <div>
//               <label htmlFor="country" className="block text-sm font-medium text-gray-700">Country</label>
//               <select id="country" value={formData.country} onChange={handleChange} className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm">
//                 <option value="us">United States</option>
//                 <option value="ca">Canada</option>
//                 <option value="uk">United Kingdom</option>
//                 <option value="au">Australia</option>
//                 <option value="de">Germany</option>
//               </select>
//             </div>
//             <div>
//               <label htmlFor="line1" className="block text-sm font-medium text-gray-700">Address Line 1</label>
//               <input id="line1" type="text" value={formData.line1} onChange={handleChange} placeholder="123 Main St" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
//             </div>
//             <div>
//               <label htmlFor="line2" className="block text-sm font-medium text-gray-700">Address Line 2</label>
//               <input id="line2" type="text" value={formData.line2} onChange={handleChange} placeholder="Apartment, suite, etc." className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
//             </div>
//             <div className="flex gap-4">
//               <div className="flex-1">
//                 <label htmlFor="city" className="block text-sm font-medium text-gray-700">City</label>
//                 <input id="city" type="text" value={formData.city} onChange={handleChange} placeholder="San Francisco" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
//               </div>
//               <div className="flex-1">
//                 <label htmlFor="state" className="block text-sm font-medium text-gray-700">State</label>
//                 <input id="state" type="text" value={formData.state} onChange={handleChange} placeholder="CA" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
//               </div>
//               <div className="flex-1">
//                 <label htmlFor="postalCode" className="block text-sm font-medium text-gray-700">Postal Code</label>
//                 <input id="postalCode" type="text" value={formData.postalCode} onChange={handleChange} placeholder="94101" className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-500 sm:text-sm" />
//               </div>
//             </div>
//             <div className="flex justify-between p-4 border-t border-gray-200">
//               <button type="button" onClick={onClose} className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200">Close</button>
//               <button type="submit" className="px-4 py-2 text-sm font-medium text-white bg-gray-900 rounded-md hover:bg-gray-800">Make Payment</button>
//             </div>
//           </form>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default ShippingAddressModal;
