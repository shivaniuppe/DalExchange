import React, { useEffect, useState } from "react";
import { ReviewsApi } from "../../services/ReviewsApi"; 
import Header from "../../components/Header";
import Loader from "../../components/Loader";
import SubHeader from "../../components/SubHeader";
import ErrorAlert from "../../components/ErrorAlert";
import DataNotFound from "../../components/DataNotFound";

export default function Reviews() {
  const [reviews, setReviews] = useState([]);
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
    const fetchProductRatings = async () => {
      try {
        setIsLoading(true);
        const params = new URLSearchParams(window.location.search);
        const productId = params.get("id");
        console.log({ productId });

        const data = await ReviewsApi.fetchProductRatings(productId);
        setReviews(data);
        console.log(data, "product_ratings");
      } catch (error) {
        console.error("Failed to fetch product data", error);
        setError(error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchProductRatings();
  }, []);

  return (
    <div className="bg-gray-100 h-screen max-h-100">
      <Header config={headerConfig} />
      <SubHeader title={"Reviews"} backPath={"/profile"} />
      {isLoading && <Loader title={"There is no Review"} />}
      {!isLoading && error && <ErrorAlert message={error.message} />}
      {!isLoading && !error && reviews && reviews.length > 0 ? (
        <div className="py-8 mt-50">
          <div className="border rounded-lg shadow-sm m-4">
            <table className="w-full text-sm text-left rtl:text-right text-gray-500">
              <thead className="text-xs text-gray-700 uppercase bg-gray-50">
                <tr>
                  <th className="px-6 py-3">No.</th>
                  <th className="px-6 py-3">Title</th>
                  <th className="px-6 py-3">Review</th>
                  <th className="px-6 py-3">Rating</th>
                </tr>
              </thead>
              <tbody>
                {reviews.map((item, index) => (
                  <tr
                    className="bg-white border-b"
                    key={index}
                  >
                    <td className="px-6 py-4">{index + 1}</td>
                    <td className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                      <div className="flex items-center gap-3">
                        <div>{item?.title}</div>
                      </div>
                    </td>
                    <td className="px-6 py-4">{item?.review}</td>
                    <td className="px-6 py-3">
                      <div className="flex items-center gap-px">
                        <StarRating starCount={item?.rating} />
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div className="my-20">
          <DataNotFound message={"Oops! No items sold yet."} />
        </div>
      )}
    </div>
  );
}

const FilledStarIcon = (props) => {
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
      <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
    </svg>
  );
};

const StarRating = ({ starCount }) => {
  const totalStars = 5;
  const stars = [];

  for (let i = 0; i < totalStars; i++) {
    if (i < starCount) {
      stars.push(<FilledStarIcon key={i} />);
    } else {
      stars.push(<StarIcon key={i} />);
    }
  }

  return <div className="flex flex-row">{stars}</div>;
};

const StarIcon = (props) => {
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
};