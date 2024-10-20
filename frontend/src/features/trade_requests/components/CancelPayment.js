import React from "react";
import Header from '../../../components/Header';
import CircalIcon from "../../../assets/icons/circle-xmark-solid.svg"
import SubHeader from '../../../components/SubHeader';
import { Link } from "react-router-dom";

export default function PaymentCancelled() {

  const headerConfig = {
    search: false,
    requests: true,
    notifications: true,
    add: true,
    profile: true
  };

  return (
    <div className="flex flex-col min-h-[100dvh]">
      <Header config={headerConfig} />
      <SubHeader title="Cancel Payment" backPath="/trade_requests" />

      <div className="flex items-center justify-center min-h-screen bg-gray-100 px-4 py-100 sm:px-6 lg:px-8">
        <div className="max-w-md mx-auto">
          <div className="bg-white shadow-md rounded-lg overflow-hidden">
            <div className="p-6 text-center">
              <img
                alt="Cancellation icon"
                className="w-12 h-12 mx-auto mb-4"
                src={CircalIcon}
              />
              <h1 className="text-3xl font-bold tracking-tight text-gray-900 mb-4">
                Payment Cancelled
              </h1>
              <p className="text-gray-600 mb-6">
                Your payment has been cancelled. Please try again later.
              </p>
              <div className="flex justify-center gap-4">
                <Link
                  to="/"
                  className="inline-flex items-center rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition-colors hover:bg-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2"
                >
                  Go to Homepage
                </Link>
                <button
                  className="inline-flex items-center rounded-md border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 transition-colors hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:ring-offset-2"
                  onClick={() => window.location.replace('/trade_requests')}
                >
                  Try Again
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
