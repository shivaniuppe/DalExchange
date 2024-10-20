import React, { useEffect, useState } from "react";
import Header from "../../components/Header";
import { TradeRequestApi } from "../../services/TradeRequestApi";
import SubHeader from "../../components/SubHeader";
import Loader from "../../components/Loader";
import ErrorAlert from "../../components/ErrorAlert";
import placeholder from "../../assets/images/placeholder.png";
import UserPlaceholder from "../../assets/images/placeholder-user.jpg";
import StartRating from "../../components/StartRating";
import ShippingAddressModal from "./components/ShippingAddressModal";

const TradeRequests = () => {
  const [activeTab, setActiveTab] = useState("buy");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const [buyRequests, setBuyRequests] = useState([]);
  const [sellRequests, setSellRequests] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState({});

  const headerConfig = {
    search: false,
    requests: true,
    notifications: true,
    add: true,
    profile: true
  };

  useEffect(() => {
    const fetchTradeRequests = async () => {
      const setters = {
        buyRequests: setBuyRequests,
        sellRequests: setSellRequests,
        isLoading: setIsLoading,
        error: setError
      };
        await TradeRequestApi.getTradeRequests(setters);
    };
    fetchTradeRequests();
  }, []);

  const handleStatusUpdate = (id, status, type) => {

    TradeRequestApi.updateStatus(id, status).then(() => {
      if(type === "buy") {
        const updatedBuyRequests = buyRequests.map((request) => {
          if (request.requestId === id) {
            return { ...request, requestStatus: status }
          }
          return request;
        });
        setBuyRequests(updatedBuyRequests);
      } else if(type === "sell") {
        const updatedSellRequests = sellRequests.map((request) => {
          if (request.requestId === id) {
            return { ...request, requestStatus: status }
          }
          return request;
        });
        setSellRequests(updatedSellRequests);
      }
    }).catch((error) => {
        console.error("Error updating trade request status: ", error);
        alert("Error updating trade request status!");
    });
  }

  const rejectBuyRequest = (id) => {
    handleStatusUpdate(id, "canceled", "buy");
  }

  const acceptSellRequest = (id) => {
    handleStatusUpdate(id, "approved", "sell");
  }

  const rejectSellRequest = (id) => {
    handleStatusUpdate(id, "rejected", "sell");
  }

  const openModal = (tradeRequest) => {
    setSelectedRequest(tradeRequest);
    setIsModalOpen(true);
  }

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedRequest({});
  };

  console.log("buyRequests", buyRequests, buyRequests[0 ]);
  console.log("tradeRequests", buyRequests,buyRequests[1]);

  return (
    <div className="flex flex-col min-h-[100dvh]">
      <Header config={headerConfig}></Header>
      <SubHeader title={'Trade Requests'} backPath={'/products'}></SubHeader>
      {isLoading && <Loader title={'Loading Trade Requests...'} />}
      {!isLoading && error && <ErrorAlert message={error.message} />}
      {!isLoading && !error && isModalOpen && <ShippingAddressModal tradeRequest={selectedRequest} onClose={closeModal}/>}
      {!isLoading && !error && <main>
        <div className="w-full max-w-6xl mx-auto md:px-6 pb-8">
          <div>
          <div className="my-8">
            <ul className="text-sm font-medium text-center text-gray-500 rounded-lg shadow sm:flex">
              <li className="w-full focus-within:z-10" onClick={() => setActiveTab('buy')}>
                <div className={`inline-block w-full p-4 border-2 border-gray-900 rounded-s-lg focus:outline-none ${activeTab === 'buy' ? 'text-gray-100 bg-gray-900' : 'text-gray-900 bg-gray-100' }`}>
                  Buy Requests
                </div>
              </li>
              <li className="w-full focus-within:z-10" onClick={() => setActiveTab('sell')}>
                <div className={`inline-block w-full p-4 border-2 border-gray-900 rounded-e-lg focus:outline-none ${activeTab === 'sell' ? 'text-gray-100 bg-gray-900' : 'text-gray-900 bg-gray-100' }`}>
                  Sell Requests
                </div>
              </li>
            </ul>
          </div>
          {activeTab === 'buy' && buyRequests.map((tradeRequest) => (
            <div key={tradeRequest.requestId} className="flex items-center mb-4 bg-white border border-gray-200 rounded-lg shadow md:flex-row hover:bg-gray-100">
              <img className="basis-1/6 object-cover md:h-48 md:w-48 md:rounded-lg ml-2"
                src={tradeRequest?.product?.imageUrl ? tradeRequest.product.imageUrl : placeholder} alt=""/>
              <div className="flex flex-col basis-5/6 justify-between p-4 leading-normal">
                <div className="flex justify-between">
                  <div>
                    <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900">{tradeRequest.product.title}</h5>
                    <p className="mb-3 font-normal text-gray-700 min-h-12">{tradeRequest.product.description}</p>
                    <div className="flex items-center gap-4">
                      <div>
                        <div className="flex mb-1">
                        <img
                            className="w-10 h-10 rounded-full border-2" alt=""
                            src={tradeRequest.sellerImage ? tradeRequest.sellerImage : UserPlaceholder} />
                          <div className="font-medium ml-2">
                              <div>{tradeRequest.sellerName}</div>
                              {tradeRequest.sellerJoiningDate && <div className="text-sm text-gray-500">
                                Joined at {tradeRequest.sellerJoiningDate[2]}/{tradeRequest.sellerJoiningDate[1]}/{tradeRequest.sellerJoiningDate[0]}
                              </div>}
                          </div>
                        </div>
                        <StartRating totalStars={tradeRequest?.sellerRating || 0}></StartRating>
                      </div>
                      <div className="text-xs font-medium ml-4">
                        <span className="bg-gray-100 text-gray-800 h-6 px-2 py-0.5 rounded-full border border-gray-400 mr-1">{tradeRequest.product.categoryName}</span>
                        <span className="bg-gray-100 text-gray-800 h-6 px-2 py-0.5 rounded-full border border-gray-400 mr-1">{tradeRequest.product.productCondition}</span>
                        {tradeRequest.product.shippingType === 'free' && 
                          <span className="bg-gray-100 text-gray-800 h-6 px-2 py-0.5 rounded-full border border-gray-400 mr-1">Free Shipping</span>}
                        {tradeRequest.requestStatus === 'pending' &&
                          <span className="bg-yellow-100 text-yellow-800 h-6 px-2 py-0.5 rounded-full border border-yellow-400 mr-1">Pending Approval</span>}
                        {tradeRequest.requestStatus === 'approved' &&
                          <span className="bg-green-100 text-green-800 h-6 px-2 py-0.5 rounded-full border border-green-400 mr-1">Approved</span>}
                        {tradeRequest.requestStatus === 'rejected' &&
                          <span className="bg-red-100 text-red-800 h-6 px-2 py-0.5 rounded-full border border-red-400 mr-1">Rejected</span>}
                        {tradeRequest.requestStatus === 'completed' &&
                          <span className="bg-green-100 text-green-800 h-6 px-2 py-0.5 rounded-full border border-green-400 mr-1">Completed</span>}
                        {tradeRequest.requestStatus === 'canceled' &&
                          <span className="bg-red-100 text-red-800 h-6 px-2 py-0.5 rounded-full border border-red-400 mr-1">Canceled</span>}
                      </div>
                    </div>
                  </div>
                  <div className="flex flex-col justify-between">
                    <div className="flex justify-end">
                      <div className="ml-2 px-3 py-2 rounded-lg border border-gray-300 max-h-24">
                        <div className="flex flex-col items-center justify-end">
                          <dt className="mb-2 text-3xl font-medium">${tradeRequest.product.price}</dt>
                          <dd className="text-gray-800">Actual</dd>
                        </div>
                      </div>
                      <div className="ml-2 px-3 py-2 rounded-lg border border-gray-300 max-h-24">
                        <div className="flex flex-col items-center justify-end">
                          <dt className="mb-2 text-3xl font-medium">${tradeRequest.requestedPrice}</dt>
                          <dd className="text-gray-800">Requested</dd>
                        </div>
                      </div>
                    </div>
                    <div>
                      <div className="flex justify-end gap-2 items-end w-full">
                        {tradeRequest.requestStatus === 'approved' &&
                        <button type="button" 
                          onClick={() => openModal(tradeRequest) }
                          className="text-white bg-gray-900 hover:bg-gray-800 focus:outline-none font-medium rounded-lg text-sm px-8 py-2.5">
                          Buy
                        </button>}
                        {tradeRequest.requestStatus === 'approved' &&
                        <button type="button"
                          onClick={() => rejectBuyRequest(tradeRequest?.requestId)}
                          className="text-gray-900 bg-white border border-gray-300 focus:outline-none hover:bg-gray-100 font-medium rounded-lg text-sm px-8 py-2.5">
                          Cancel
                        </button>}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          ))}


          {activeTab === 'sell' && sellRequests.map((tradeRequest) => (
            <div key={tradeRequest.requestId} className="flex items-center mb-4 bg-white border border-gray-200 rounded-lg shadow md:flex-row hover:bg-gray-100">
            <img className="basis-1/6 object-cover md:h-48 md:w-48 md:rounded-lg ml-2"
                src={tradeRequest?.product?.imageUrl ? tradeRequest.product.imageUrl : placeholder} alt=""/>
              <div className="flex flex-col basis-5/6 justify-between p-4 leading-normal">
                <div className="flex justify-between">
                  <div>
                    <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900">{tradeRequest.product.title}</h5>
                    <p className="mb-3 font-normal text-gray-700 min-h-12">{tradeRequest.product.description}</p>
                    <div className="flex items-center gap-4">
                      <div>
                        <div className="flex mb-1">
                          <img
                            className="w-10 h-10 rounded-full border-2" alt=""
                            src={tradeRequest.buyerImage ? tradeRequest.buyerImage : UserPlaceholder} />
                          <div className="font-medium ml-2">
                              <div>{tradeRequest.buyerName}</div>
                              {tradeRequest.buyerJoiningDate && <div className="text-sm text-gray-500">
                                Joined at {tradeRequest.buyerJoiningDate[2]}/{tradeRequest.buyerJoiningDate[1]}/{tradeRequest.buyerJoiningDate[0]}
                              </div>}
                          </div>
                        </div>
                        <StartRating totalStars={tradeRequest?.buyerNameRating || 0}></StartRating>
                      </div>
                      <div className="text-xs font-medium ml-4">
                        <span className="bg-gray-100 text-gray-800 h-6 px-2 py-0.5 rounded-full border border-gray-400 mr-1">{tradeRequest.product.categoryName}</span>
                        <span className="bg-gray-100 text-gray-800 h-6 px-2 py-0.5 rounded-full border border-gray-400 mr-1">{tradeRequest.product.productCondition}</span>
                        {tradeRequest.product.shippingType === 'free' && 
                          <span className="bg-gray-100 text-gray-800 h-6 px-2 py-0.5 rounded-full border border-gray-400 mr-1">Free Shipping</span>}
                        {tradeRequest.requestStatus === 'pending' &&
                          <span className="bg-yellow-100 text-yellow-800 h-6 px-2 py-0.5 rounded-full border border-yellow-400 mr-1">Pending Approval</span>}
                        {tradeRequest.requestStatus === 'approved' &&
                          <span className="bg-green-100 text-green-800 h-6 px-2 py-0.5 rounded-full border border-green-400 mr-1">Approved</span>}
                        {tradeRequest.requestStatus === 'rejected' &&
                          <span className="bg-red-100 text-red-800 h-6 px-2 py-0.5 rounded-full border border-red-400 mr-1">Rejected</span>}
                        {tradeRequest.requestStatus === 'completed' &&
                          <span className="bg-green-100 text-green-800 h-6 px-2 py-0.5 rounded-full border border-green-400 mr-1">Completed</span>}
                        {tradeRequest.requestStatus === 'canceled' &&
                          <span className="bg-red-100 text-red-800 h-6 px-2 py-0.5 rounded-full border border-red-400 mr-1">Canceled</span>}
                      </div>
                    </div>
                  </div>
                  <div className="flex flex-col justify-between">
                    <div className="flex justify-end">
                      <div className="ml-2 px-3 py-2 rounded-lg border border-gray-300 max-h-24">
                        <div className="flex flex-col items-center justify-end">
                          <dt className="mb-2 text-3xl font-medium">${tradeRequest.product.price}</dt>
                          <dd className="text-gray-800">Actual</dd>
                        </div>
                      </div>
                      <div className="ml-2 px-3 py-2 rounded-lg border border-gray-300 max-h-24">
                        <div className="flex flex-col items-center justify-end">
                          <dt className="mb-2 text-3xl font-medium">${tradeRequest.requestedPrice}</dt>
                          <dd className="text-gray-800">Requested</dd>
                        </div>
                      </div>
                    </div>
                    <div>
                      <div className="flex justify-end gap-2 items-end w-full">
                        {tradeRequest.requestStatus === 'pending' &&
                        <button type="button" 
                          onClick={() => acceptSellRequest(tradeRequest.requestId)}
                          className="text-white bg-gray-900 hover:bg-gray-800 focus:outline-none font-medium rounded-lg text-sm px-8 py-2.5">
                          Accept
                        </button>}
                        {tradeRequest.requestStatus === 'pending' &&
                        <button type="button"
                          onClick={() => rejectSellRequest(tradeRequest.requestId)}
                          className="text-gray-900 bg-white border border-gray-300 focus:outline-none hover:bg-gray-100 font-medium rounded-lg text-sm px-8 py-2.5">
                          Reject
                        </button>}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          ))}
          </div>
        </div>
      </main>}
    </div>
  )
}

export default TradeRequests;