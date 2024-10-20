import React from "react";
import placeholder from "../../../assets/images/placeholder.png";
import { Link } from "react-router-dom";

const ProductCard = ({ product }) => {
  return (
    <Link to={`/products/${product.productId}`} className="bg-white rounded-lg shadow-md overflow-hidden h-96">
      <img
        alt=""
        className="w-full h-48 object-cover"
        height={300}
        src={product.imageUrl ? product.imageUrl : placeholder}
        style={{
          aspectRatio: "400/300",
          objectFit: "cover",
        }}
        width={400}
      />
      <div className="flex flex-col justify-between p-4 max-h-48 h-full">
        <div className="flex justify-between">
          <h3 className="text-lg font-medium mb-2">{product.title.slice(0,14)}</h3>
          <h3 className="text-xl font-semibold">${product.price}</h3>
        </div>
        <p className="text-gray-600 text-sm mb-2 grow">
          {(product.description).slice(0,64)}...
        </p>
        <div className="text-gray-800 text-xs font-medium mb-2">
          <span className="bg-gray-100 h-6 px-2 py-0.5 rounded-full border border-gray-400 mr-1">{product.categoryName}</span>
          <span className="bg-gray-100 h-6 px-2 py-0.5 rounded-full border border-gray-400 mr-1">{product.productCondition}</span>
          {product.shippingType === 'free' && <span 
            className="bg-gray-100 h-6 px-2 py-0.5 rounded-full border border-gray-400">
            Free shipping
          </span>}
        </div>
        <p className="text-gray-600 text-sm mb-1">
            Use Duration: {product.useDuration}
        </p>
        <p className="text-gray-600 text-sm">
            Posted on {product.createdAt[2]}/{product.createdAt[1]}/{product.createdAt[0]}
        </p>
      </div>
    </Link>
  );
};

export default ProductCard;
