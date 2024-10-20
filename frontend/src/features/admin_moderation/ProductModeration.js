import { useState, useEffect } from "react";
import ProductModerationApi from "../../services/ProductModerationApi";

export default function ProductModeration() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [editedProduct, setEditedProduct] = useState(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const products = await ProductModerationApi.fetchProducts();
        setProducts(products);
      } catch (error) {
        console.error("There was an error fetching the products!", error);
      }
    };

    const fetchCategories = async () => {
      try {
        const categories = await ProductModerationApi.fetchCategories();
        setCategories(categories);
      } catch (error) {
        console.error("There was an error fetching the categories!", error);
      }
    };

    fetchProducts();
    fetchCategories();
  }, []);

  const handleProductSelect = async (productId) => {
    try {
      const productDetails = await ProductModerationApi.fetchProductDetails(productId);
      setSelectedProduct(productDetails);
      setEditedProduct({ ...productDetails });
    } catch (error) {
      console.error("There was an error fetching the product details!", error);
    }
  };

  const handleProductUpdate = async () => {
    try {
      await ProductModerationApi.updateProduct(editedProduct.productId, editedProduct);
      setProducts(products.map((product) => (product.productId === editedProduct.productId ? editedProduct : product)));
      setSelectedProduct(null);
      setEditedProduct(null);
    } catch (error) {
      console.error("There was an error updating the product!", error);
    }
  };

  const handleProductUnlist = async (productId, unlisted) => {
    try {
      await ProductModerationApi.unlistProduct(productId, unlisted);
      setProducts(products.map((product) =>
        product.productId === productId ? { ...product, unlisted: unlisted } : product,
      ));
      setSelectedProduct(null);
      setEditedProduct(null);
    } catch (error) {
      console.error("There was an error unlisting the product!", error);
    }
  };

  const toPascalCase = (str) => {
    return str
      .toLowerCase()
      .replace(/_/g, ' ')
      .replace(/(?:^|\s)(\w)/g, (_, c) => c ? c.toUpperCase() : '');
  };

  return (
    <div className="flex flex-col min-h-screen w-full">
      <header className="flex h-14 lg:h-[60px] items-center gap-4 border-b bg-gray-200 px-6 mx-4 mt-4 rounded-lg">
        <div className="w-full flex justify-center">
          <h1 className="text-3xl font-bold">Product Moderation</h1>
        </div>
      </header>
      <main className="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-6">
        {selectedProduct ? (
          <div>
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-2xl font-bold">Edit Product Details</h2>
              <button className="px-4 py-2 bg-gray-500 text-white rounded" onClick={() => setSelectedProduct(null)}>
                Back to Products
              </button>
            </div>
            <div className="grid md:grid-cols-2 gap-6">
              <div className="border border-gray-300 rounded-lg bg-white text-black p-6">
                <div className="mb-4">
                  <h2 className="text-lg font-bold text-black border-b pb-2">Product Details</h2>
                </div>
                <div className="grid gap-4">
                  <div className="grid gap-1">
                    <div className="text-black font-bold">Title</div>
                    <div className="font-normal">{selectedProduct.title}</div>
                  </div>
                  <div className="grid gap-1">
                    <div className="text-black font-bold">Description</div>
                    <div className="font-normal">{selectedProduct.description}</div>
                  </div>
                  <div className="grid gap-1">
                    <div className="text-black font-bold">Category</div>
                    <div className="font-normal">{selectedProduct.category ? selectedProduct.category.name : 'No Category'}</div>
                  </div>
                  <div className="grid gap-1">
                    <div className="text-black font-bold">Condition</div>
                    <div className="font-normal">{toPascalCase(selectedProduct.productCondition)}</div>
                  </div>
                  <div className="grid gap-1">
                    <div className="text-black font-bold">Use Duration (months)</div>
                    <div className="font-normal">{selectedProduct.useDuration}</div>
                  </div>
                  <div className="grid gap-1">
                    <div className="text-black font-bold">Shipping Type</div>
                    <div className="font-normal">{toPascalCase(selectedProduct.shippingType)}</div>
                  </div>
                  <div className="grid gap-1">
                    <div className="text-black font-bold">Quantity</div>
                    <div className="font-normal">{selectedProduct.quantityAvailable}</div>
                  </div>
                  <div className="grid gap-1">
                    <div className="text-black font-bold">Price</div>
                    <div className="font-normal">${selectedProduct.price.toFixed(2)}</div>
                  </div>
                </div>
              </div>
              <div className="border border-gray-300 rounded-lg bg-white text-black p-6">
                <div className="mb-4">
                  <h2 className="text-lg font-bold text-black border-b pb-2">Edit Product Details</h2>
                </div>
                <div className="grid gap-4">
                  <div className="grid gap-2">
                    <label htmlFor="title" className="font-bold">Title</label>
                    <input
                      id="title"
                      value={editedProduct.title}
                      onChange={(e) => setEditedProduct({ ...editedProduct, title: e.target.value })}
                      className="w-full p-2 border border-gray-300 rounded"
                    />
                  </div>
                  <div className="grid gap-2">
                    <label htmlFor="description" className="font-bold">Description</label>
                    <textarea
                      id="description"
                      value={editedProduct.description}
                      onChange={(e) =>
                        setEditedProduct({
                          ...editedProduct,
                          description: e.target.value,
                        })
                      }
                      className="w-full p-2 border border-gray-300 rounded"
                    />
                  </div>
                  <div className="grid gap-2">
                    <label htmlFor="category" className="font-bold">Category</label>
                    <select
                      id="category"
                      value={editedProduct.category ? editedProduct.category.categoryId : ''}
                      onChange={(e) => {
                        const selectedCategory = categories.find(category => category.categoryId === parseInt(e.target.value));
                        setEditedProduct({ ...editedProduct, category: selectedCategory });
                      }}
                      className="mt-1 block w-full p-2 border border-gray-300 rounded"
                    >
                      {categories.map((category) => (
                        <option key={category.categoryId} value={category.categoryId}>{category.name}</option>
                      ))}
                    </select>
                  </div>
                  <div className="grid gap-2">
                    <label htmlFor="condition" className="font-bold">Condition</label>
                    <select
                      id="condition"
                      value={editedProduct.productCondition}
                      onChange={(e) => setEditedProduct({ ...editedProduct, productCondition: e.target.value })}
                      className="w-full p-2 border border-gray-300 rounded"
                    >
                      <option value="New">New</option>
                      <option value="Good">Good</option>
                      <option value="Fair">Fair</option>
                      <option value="Used">Used</option>
                      <option value="Poor">Poor</option>
                    </select>
                  </div>
                  <div className="grid gap-2">
                    <label htmlFor="useDuration" className="font-bold">Use Duration (months)</label>
                    <input
                      id="useDuration"
                      type="text"
                      value={editedProduct.useDuration}
                      onChange={(e) =>
                        setEditedProduct({
                          ...editedProduct,
                          useDuration: e.target.value,
                        })
                      }
                      className="w-full p-2 border border-gray-300 rounded"
                    />
                  </div>
                  <div className="grid gap-2">
                    <label htmlFor="shippingType" className="font-bold">Shipping Type</label>
                    <select
                      id="shippingType"
                      value={editedProduct.shippingType}
                      onChange={(e) => setEditedProduct({ ...editedProduct, shippingType: e.target.value })}
                      className="w-full p-2 border border-gray-300 rounded"
                    >
                      <option value="Free">Free</option>
                      <option value="Paid">Paid</option>
                    </select>
                  </div>
                  <div className="grid gap-2">
                    <label htmlFor="quantity" className="font-bold">Quantity</label>
                    <input
                      id="quantity"
                      type="number"
                      value={editedProduct.quantityAvailable}
                      onChange={(e) =>
                        setEditedProduct({
                          ...editedProduct,
                          quantityAvailable: parseInt(e.target.value),
                        })
                      }
                      className="w-full p-2 border border-gray-300 rounded"
                    />
                  </div>
                  <div className="grid gap-2">
                    <label htmlFor="price" className="font-bold">Price</label>
                    <input
                      id="price"
                      type="number"
                      value={editedProduct.price}
                      onChange={(e) =>
                        setEditedProduct({
                          ...editedProduct,
                          price: parseFloat(e.target.value),
                        })
                      }
                      className="w-full p-2 border border-gray-300 rounded"
                    />
                  </div>
                  <div className="flex justify-between gap-2 mt-4">
                    <button
                      className="px-4 py-2 w-full border border-gray-300 rounded"
                      onClick={() => {
                        setSelectedProduct(null);
                        setEditedProduct(null);
                      }}
                    >
                      Cancel
                    </button>
                    <button
                      className={`px-4 py-2 w-full text-sm font-medium ${
                        editedProduct.unlisted ? "bg-green-500 text-white" : "bg-red-500 text-white"
                      } rounded`}
                      onClick={() => handleProductUnlist(editedProduct.productId, !editedProduct.unlisted)}
                    >
                      {editedProduct.unlisted ? "List" : "Unlist"}
                    </button>
                    <button
                      className="px-4 py-2 w-full bg-black text-white rounded"
                      onClick={handleProductUpdate}
                    >
                      Save
                    </button>
                  </div>
                </div>
              </div>
            </div>
            </div>
          ) : (
            <div className="bg-white rounded-lg shadow-md p-6">
              <table className="min-w-full divide-y divide-gray-200">
                <thead>
                  <tr>
                    <th className="px-6 py-3 bg-gray-50 text-left text-xs font-bold text-black uppercase tracking-wider">Title</th>
                    <th className="px-6 py-3 bg-gray-50 text-left text-xs font-bold text-black uppercase tracking-wider">Category</th>
                    <th className="px-6 py-3 bg-gray-50 text-left text-xs font-bold text-black uppercase tracking-wider">Condition</th>
                    <th className="px-6 py-3 bg-gray-50 text-left text-xs font-bold text-black uppercase tracking-wider">Quantity</th>
                    <th className="px-6 py-3 bg-gray-50 text-left text-xs font-bold text-black uppercase tracking-wider">Price</th>
                    <th className="px-6 py-3 bg-gray-50 text-left text-xs font-bold text-black uppercase tracking-wider">Actions</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {products.map((product) => (
                    <tr key={product.productId} onClick={() => handleProductSelect(product.productId)} className="cursor-pointer hover:bg-gray-100">
                      <td className="px-6 py-4 whitespace-nowrap">{product.title}</td>
                      <td className="px-6 py-4 whitespace-nowrap">{product.category.name}</td>
                      <td className="px-6 py-4 whitespace-nowrap">{toPascalCase(product.productCondition)}</td>
                      <td className="px-6 py-4 whitespace-nowrap">{product.quantityAvailable}</td>
                      <td className="px-6 py-4 whitespace-nowrap">${product.price.toFixed(2)}</td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <button
                          className={`px-4 py-2 text-sm font-medium ${
                            product.unlisted ? "bg-green-500 text-white" : "bg-red-500 text-white"
                          } rounded`}
                          onClick={(e) => {
                            e.stopPropagation();
                            handleProductUnlist(product.productId, !product.unlisted);
                          }}
                        >
                          {product.unlisted ? "List" : "Unlist"}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </main>
      </div>
  );
}
