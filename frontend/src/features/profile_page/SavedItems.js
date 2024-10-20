import React, { useEffect, useState } from "react";
import Header from "../../components/Header";
import Loader from "../../components/Loader";
import SubHeader from "../../components/SubHeader";
import ErrorAlert from "../../components/ErrorAlert";
import DataNotFound from "../../components/DataNotFound";
import SavedItemsApi from "../../services/SavedItemsApi";
import toast from 'react-hot-toast';

export default function SavedItems() {
  const [isLoading, setIsLoading] = useState(false);
  const [savedProducts, setSavedProducts] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedProductId, setSelectedProductId] = useState(null);
  const [error, setError] = useState(null);
  const headerConfig = {
    search: false,
    requests: true,
    notifications: true,
    add: true,
    profile: true,
  };

  useEffect(() => {
    fetchedSavedProducts();
  }, []);

  const fetchedSavedProducts = async () => {
    try {
      setIsLoading(true);
      const data = await SavedItemsApi.fetchSavedItems();
      setSavedProducts(data);
    } catch (error) {
      console.error("Failed to fetch saved products", error);
      setError(error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRemoveClick = (productId) => {
    setSelectedProductId(productId);
    setIsModalOpen(true);
  };

  const confirmRemoveFavorite = async (confirm) => {
    if (confirm) {
      await removeFavorite(selectedProductId);
    }
    setIsModalOpen(false);
    setSelectedProductId(null);
  };

  const removeFavorite = async (productId) => {
    try {
      await SavedItemsApi.removeSavedItem(productId);
      toast.success("Saved item removed successfully!");
      fetchedSavedProducts(); // Refresh saved items after removal
    } catch (error) {
      console.error("Failed to remove saved item", error);
      toast.error("Failed to remove saved item.");
    }
  };

  return (
    <>
      <div className="bg-gray-100 h-screen max-h-100">
        <Header config={headerConfig} />
        <SubHeader title={"Saved Items"} backPath={"/profile"} />
        {isLoading && <Loader title={"Loading Saved Items..."} />}
        {!isLoading && error && <ErrorAlert message={error.message} />}
        {!isLoading && !error && savedProducts && savedProducts.length > 0 ? (
          <div className="border rounded-lg shadow-sm m-4">
            <table className="w-full text-sm text-left rtl:text-right text-gray-500">
              <thead className="px-4 text-xs text-gray-700 uppercase bg-gray-50">
                <tr>
                  <th className="px-6 py-3">Id</th>
                  <th className="px-6 py-3">Title</th>
                  <th className="px-6 py-3">Price</th>
                  <th className="px-6 py-3">Category</th>
                  <th className="px-6 py-3">Product Condition</th>
                  <th className="px-6 py-3">Use Duration</th>
                  <th className="px-6 py-3">Quantity Available</th>
                  <th className="px-6 py-3">Favorite</th>
                </tr>
              </thead>
              <tbody>
                {savedProducts.map((item, index) => (
                  <tr
                    className="bg-white border-b"
                    key={index}
                  >
                    <td className="px-6 py-4">{index + 1}</td>
                    <td className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                      <div className="flex items-center gap-3">
                        <div>{item.title}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4">{item.price}</td>
                    <td className="px-6 py-3">{item.category}</td>
                    <td className="px-6 py-3">{item.productCondition}</td>
                    <td className="px-6 py-3">{item.useDuration}</td>
                    <td className="px-6 py-3">{item.quantityAvailable}</td>
                    <td
                      className="px-6 py-3 cursor-pointer"
                      onClick={() => {
                        handleRemoveClick(item.productId);
                      }}
                    >
                      <FilledHeartIcon />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="my-20">
            <DataNotFound message={"Oops! No saved items yet."} />
          </div>
        )}
      </div>
      {isModalOpen && (
        <ConfirmationModal
          message="Are you sure you want to remove this item from your favorites?"
          onConfirm={() => confirmRemoveFavorite(true)}
          onCancel={() => confirmRemoveFavorite(false)}
        />
      )}
    </>
  );
}

const FilledHeartIcon = (props) => {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="currentColor"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z" />
    </svg>
  );
};

const ConfirmationModal = ({ message, onConfirm, onCancel }) => {
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
      <div className="bg-white p-6 rounded-md shadow-md">
        <p className="mb-4">{message}</p>
        <div className="flex justify-end">
          <button
            onClick={onConfirm}
            className="bg-red-500 text-white px-4 py-2 rounded-md mr-2"
          >
            Confirm
          </button>
          <button
            onClick={onCancel}
            className="bg-gray-300 px-4 py-2 rounded-md"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
};
