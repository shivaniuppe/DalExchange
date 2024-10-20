import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import Header from '../../../components/Header';
import SuccessIcon from "../../../assets/icons/circle-check-solid.svg";
import SubHeader from '../../../components/SubHeader';
import { TradeRequestApi } from '../../../services/TradeRequestApi';

const StarRating = ({ rating, onRatingChange }) => {
  const handleClick = (value) => {
    onRatingChange(value);
  };

  return (
    <div className="flex items-center">
      {[...Array(5)].map((_, index) => {
        const starRating = index + 1;
        return (
          <StarIcon
            key={index}
            className={`h-6 w-6 cursor-pointer ${
              starRating <= rating ? 'text-black-500' : 'text-gray-300'
            }`}
            onClick={() => handleClick(starRating)}
          />
        );
      })}
    </div>
  );
};

const SuccessPage = () => {
  const [rating, setRating] = useState(0);
  const [review, setReview] = useState('');

  const headerConfig = {
    search: false,
    requests: true,
    notifications: true,
    add: true,
    profile: true
  };

  const handleRatingChange = (newRating) => {
    setRating(newRating);
  };

  const handleSubmit = async () => {
    console.log('Submitted Rating:', rating);
    const body = {
      rating: rating,
      review: review,
      productId: parseInt(params.get('productId')) || 0
    };
    try {
      await TradeRequestApi.saveRating(body);
      window.location.replace('/products');
    } catch (error) {
      console.error('Failed to save rating:', error);
    }
  };

  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const amount = params.get('amount');
  const orderId = params.get('orderId');
  const productId = params.get('productId');
  const paymentIntentId = params.get('paymentIntentId');

  useEffect(() => {
    const savePayment = async () => {
      const body = {
        amount: parseFloat(amount),
        productId: parseInt(productId) || 0,
        paymentIntentId: paymentIntentId || '',
        orderId: parseInt(orderId) || 0
      };

      try {
        await TradeRequestApi.savePayment(body);
      } catch (error) {
        console.error("Error saving payment:", error);
      }
    };

    const PaymentSuccess = async () => {
      const body = {
        productId: parseInt(productId) || 0,
      };

      try {
        await TradeRequestApi.paymentSuccess(body);
      } catch (error) {
        console.error("Error saving payment:", error);
      }
    };

    savePayment();
    PaymentSuccess();
  }, [amount, orderId, productId, paymentIntentId]);

  return (
    <>
      <Header config={headerConfig} />
      <SubHeader title={'Payment Successful'} backPath={'/trade_requests'} />
      <div className="flex items-center justify-center bg-gray-100 px-4 py-12 sm:px-6 lg:px-8">
        <div className="max-w-md mx-auto text-center mt-16 mb-20 bg-white p-6 rounded-lg shadow-md">
          <img alt='' className='w-12 h-12 m-auto mt' src={SuccessIcon} />
          <h1 className="mt-4 text-3xl font-bold text-gray-900 sm:text-4xl">Payment Successful</h1>
          <p className="mt-4 text-gray-600">Your payment has been processed successfully.</p>
          <div className="mt-6 flex flex-col items-center gap-4">
            <StarRating rating={rating} onRatingChange={handleRatingChange} />
            <div className="flex w-full max-w-sm items-center space-x-2">
              <textarea
                onChange={(e) => setReview(e.target.value)}
                placeholder="Write your review"
                className="flex-1 resize-none border-b border-gray-300 px-0 pb-2 text-sm focus:border-blue-500 focus:outline-none"
              />
            </div>
            <button
              onClick={handleSubmit}
              className="inline-flex items-center rounded-md bg-blue-600 px-10 py-2 text-sm font-medium text-white shadow-sm transition-colors hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
              Submit
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

function StarIcon(props) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
    </svg>
  );
}

export default SuccessPage;
