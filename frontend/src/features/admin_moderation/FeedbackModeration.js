import { useState, useEffect } from "react";
import StarIcon from "../../assets/icons/star-regular.svg";
import FilledStarIcon from "../../assets/icons/star-solid.svg";
import FeedbackModerationApi from "../../services/FeedbackModerationApi";

export default function FeedbackModeration() {
  const [products, setProducts] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const reviews = await FeedbackModerationApi.fetchReviews();

        const productIds = [...new Set(reviews.map((review) => review.productId))];
        const productResponses = await Promise.all(
          productIds.map((id) => FeedbackModerationApi.fetchProductById(id))
        );
        const products = productResponses.map((res) => res);

        const userIds = [...new Set(reviews.map((review) => review.userId))];
        const userResponses = await Promise.all(
          userIds.map((id) => FeedbackModerationApi.fetchUserById(id))
        );
        const users = userResponses.map((res) => res);

        const productMap = reviews.reduce((map, review) => {
          const product = products.find((p) => p.productId === review.productId);
          const user = users.find((u) => u.userId === review.userId);

          if (!map[review.productId]) {
            map[review.productId] = {
              id: review.productId,
              name: product ? product.title : `Product ${review.productId}`, 
              reviews: [],
            };
          }

          map[review.productId].reviews.push({
            id: review.userId,
            name: user ? user.fullName : `User ${review.userId}`, 
            rating: review.rating,
            comment: review.review,
          });
          return map;
        }, {});

        setProducts(Object.values(productMap));
      } catch (error) {
        console.error("There was an error fetching the products!", error);
      }
    };

    fetchProducts();
  }, []);

  const handleReviewDelete = async (productId, userId) => {
    try {
      await FeedbackModerationApi.deleteReview(productId, userId);
      setSelectedProduct((prevSelectedProduct) => ({
        ...prevSelectedProduct,
        reviews: prevSelectedProduct.reviews.filter((review) => review.id !== userId),
      }));
      setProducts((prevProducts) =>
        prevProducts.map((product) => {
          if (product.id === productId) {
            return {
              ...product,
              reviews: product.reviews.filter((review) => review.id !== userId),
            };
          }
          return product;
        })
      );
      setMessage("Review deleted successfully");
      setTimeout(() => setMessage(""), 3000);
    } catch (error) {
      console.error("There was an error deleting the review!", error);
      setMessage("Failed to delete the review");
      setTimeout(() => setMessage(""), 3000);
    }
  };

  return (
    <div className="flex flex-col min-h-screen w-full">
      <header className="flex h-14 lg:h-[60px] items-center gap-4 border-b bg-gray-200 px-6 mx-4 mt-4 rounded-lg">
        <div className="w-full flex justify-center">
          <h1 className="text-3xl font-bold">Feedback Moderation</h1>
        </div>
      </header>
      <main className="flex-1 p-4 md:p-6">
        {message && (
          <div className="fixed top-0 left-0 right-0 bg-black text-white p-4 z-50 text-center">
            {message}
          </div>
        )}
        {!selectedProduct ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {products.map((product) => (
              <div key={product.id} className="bg-white border border-gray-300 rounded-lg shadow-md overflow-hidden">
                <div className="p-4">
                  <h3 className="text-lg font-medium">{product.name}</h3>
                  <div className="flex justify-between items-center mt-4">
                    <div className="flex items-center gap-1">
                      {[
                        ...Array(
                          Math.round(
                            product.reviews.reduce((sum, review) => sum + review.rating, 0) / product.reviews.length
                          )
                        ),
                      ].map((_, i) => (
                        <img src={FilledStarIcon} key={i} className="w-5 h-5 fill-yellow-500" alt="Filled Star" />
                      ))}
                      {[
                        ...Array(
                          5 -
                            Math.round(
                              product.reviews.reduce((sum, review) => sum + review.rating, 0) / product.reviews.length
                            )
                        ),
                      ].map((_, i) => (
                        <img src={StarIcon} key={i} className="w-5 h-5 fill-gray-300" alt="Star" />
                      ))}
                    </div>
                    <div className="text-sm text-gray-500">{product.reviews.length} reviews</div>
                  </div>
                  <div className="flex justify-end mt-4">
                    <button className="px-4 py-2 bg-black text-white rounded hover:bg-gray-800" onClick={() => setSelectedProduct(product)}>
                      View Reviews
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div>
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-2xl font-bold">Reviews for {selectedProduct.name}</h2>
              <button className="px-4 py-2 bg-gray-500 text-white rounded" onClick={() => setSelectedProduct(null)}>
                Back to Products
              </button>
            </div>
            <div className="grid gap-6">
              {selectedProduct.reviews.map((review) => (
                <div key={review.id} className="bg-white border border-gray-300 rounded-lg shadow-md p-4">
                  <div className="flex items-center justify-between">
                    <div>
                      <h3 className="text-lg font-medium">{review.name}</h3>
                      <div className="flex items-center gap-1">
                        {[...Array(review.rating)].map((_, i) => (
                          <img src={FilledStarIcon} key={i} className="w-5 h-5 fill-yellow-500" alt="Filled Star" />
                        ))}
                        {[...Array(5 - review.rating)].map((_, i) => (
                          <img src={StarIcon} key={i} className="w-5 h-5 fill-gray-300" alt="Star" />
                        ))}
                      </div>
                    </div>
                    <button className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700" onClick={() => handleReviewDelete(selectedProduct.id, review.id)}>
                      Delete
                    </button>
                  </div>
                  <p className="mt-4">{review.comment}</p>
                </div>
              ))}
            </div>
          </div>
        )}
      </main>
    </div>
  );
}
