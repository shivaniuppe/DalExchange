import React, { useEffect, useState } from "react";
import SoldProductsApi from "../../services/SoldProductsApi";
import Header from "../../components/Header";
import DataNotFound from "../../components/DataNotFound";
import Loader from "../../components/Loader";
import SubHeader from "../../components/SubHeader";
import ErrorAlert from "../../components/ErrorAlert";

export default function SoldItems() {
  const [soldItems, setSoldItems] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const headerConfig = {
    search: false,
    requests: true,
    notifications: true,
    add: true,
    profile: true,
  };

  useEffect(() => {
    const fetchSoldItems = async () => {
      try {
        const params = new URLSearchParams(window.location.search);
        const productId = params.get("id");
        console.log({ productId });
        setIsLoading(true);

        const data = await SoldProductsApi.fetchSoldItems(productId);
        setSoldItems(data);
        console.log(data, "sold_products");
      } catch (error) {
        console.error("Failed to fetch product data", error);
        setError(error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchSoldItems();
  }, []);

  return (
    <>
      <div className="bg-gray-100 h-screen max-h-100">
        <Header config={headerConfig} />
        <SubHeader title={"Sold Items"} backPath={"/profile"} />
        {isLoading && <Loader title={"Loading Profile Details..."} />}
        {!isLoading && error && <ErrorAlert message={error.message} />}
        {!isLoading && !error && soldItems && soldItems.length > 0 ? (
          <div className="border rounded-lg shadow-sm m-4">
            <table className="w-full text-sm text-left rtl:text-right text-gray-500">
              <thead className="px-4 text-xs text-gray-700 uppercase bg-gray-50">
                <tr>
                  <th className="px-6 py-3">Id</th>
                  <th className="px-6 py-3">Title</th>
                  <th className="px-6 py-3">Price</th>
                  <th className="px-6 py-3">Date</th>
                </tr>
              </thead>
              <tbody>
                {soldItems.map((item, index) => (
                  <tr className="bg-white border-b" key={index}>
                    <td className="px-6 py-4">{item.soldItemId}</td>
                    <td className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                      <div className="flex items-center gap-3">
                        <div>{item.title}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4">{item.price}</td>
                    <td className="px-6 py-4 ">15-07-2024</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="my-20">
            <DataNotFound message={"Oops! No items sold yet."} />
          </div>
        )}
      </div>
    </>
  );
}