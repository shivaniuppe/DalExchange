import React, { useEffect, useState } from 'react';
import { Line, Bar, Doughnut } from 'react-chartjs-2';
import AdminDashboardApi from '../../services/AdminDashboardApi';
import 'chart.js/auto';

const AdminDashboard = () => {
  const [dashboardData, setDashboardData] = useState(null);
  const [itemsSoldData, setItemsSoldData] = useState(null);
  const [topSellingCategoriesData, setTopSellingCategoriesData] = useState(null);
  const [bestSellingProductsData, setBestSellingProductsData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const data = await AdminDashboardApi.fetchDashboardData();
        setDashboardData(data);
      } catch (error) {
        setError(error.message);
      }
    };

    const fetchItemsSold = async () => {
      try {
        const data = await AdminDashboardApi.fetchItemsSold();
        const months = data.map(item => item.month);
        const itemsSold = data.map(item => item.itemsSold);

        setItemsSoldData({
          labels: months,
          datasets: [
            {
              label: 'Items Sold',
              data: itemsSold,
              borderColor: '#36A2EB',
              backgroundColor: 'rgba(54, 162, 235, 0.2)',
            }
          ]
        });
      } catch (error) {
        setError(error.message);
      }
    };

    const fetchTopSellingCategories = async () => {
      try {
        const data = await AdminDashboardApi.fetchTopSellingCategories();
        const categories = data.map(item => item.category);
        const sales = data.map(item => item.sales);

        setTopSellingCategoriesData({
          labels: categories,
          datasets: [
            {
              label: 'Top Selling Categories',
              data: sales,
              backgroundColor: [
                'rgba(54, 162, 235, 0.6)',
                'rgba(255, 99, 132, 0.6)',
                'rgba(255, 206, 86, 0.6)',
                'rgba(75, 192, 192, 0.6)',
                'rgba(153, 102, 255, 0.6)',
              ],
            }
          ]
        });
      } catch (error) {
        setError(error.message);
      }
    };

    const fetchBestSellingProducts = async () => {
      try {
        const data = await AdminDashboardApi.fetchBestSellingProducts();
        const products = data.map(item => item.productName);
        const itemsSold = data.map(item => item.itemsSold);

        setBestSellingProductsData({
          labels: products,
          datasets: [
            {
              label: 'Best Selling Products',
              data: itemsSold,
              backgroundColor: 'rgba(54, 162, 235, 0.6)',
              borderColor: 'rgba(54, 162, 235, 1)',
            }
          ]
        });
      } catch (error) {
        setError(error.message);
      }
    };

    fetchDashboardData();
    fetchItemsSold();
    fetchTopSellingCategories();
    fetchBestSellingProducts();
  }, []);

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!dashboardData) {
    return <div>Loading...</div>;
  }

  const {
    customers,
    orders,
    sales,
    avgOrderValue,
    salesChange,
    ordersChange,
    customersChange,
    avgOrderValueChange,
  } = dashboardData;

  return (
    <div className="flex flex-col min-h-screen w-full">
      <header className="flex h-14 lg:h-[60px] items-center gap-4 border-b bg-gray-200 px-6 mx-4 mt-4 rounded-lg">
        <div className="w-full flex justify-center">
          <h1 className="text-3xl font-bold">Admin Dashboard</h1>
        </div>
      </header>
      <main className="flex-1 p-4 md:p-6">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <Card>
            <CardHeader>
              <CardTitle>Total Sales</CardTitle>
              <DollarSignIcon className="text-gray-500" />
            </CardHeader>
            <CardContent>
              <span className="font-bold text-2xl text-gray-700">${sales.toLocaleString()}</span>
              <span className={`ml-2 text-sm ${salesChange >= 0 ? "text-green-500" : "text-red-500"}`}>
                {salesChange >= 0 ? "+" : "-"}
                {Math.abs(salesChange).toFixed(2)}%
              </span>
            </CardContent>
          </Card>
          <Card>
            <CardHeader>
              <CardTitle>Total Orders</CardTitle>
              <ShoppingCartIcon className="text-gray-500" />
            </CardHeader>
            <CardContent>
              <span className="font-bold text-2xl text-gray-700">{orders.toLocaleString()}</span>
              <span className={`ml-2 text-sm ${ordersChange >= 0 ? "text-green-500" : "text-red-500"}`}>
                {ordersChange >= 0 ? "+" : "-"}
                {Math.abs(ordersChange).toFixed(2)}%
              </span>
            </CardContent>
          </Card>
          <Card>
            <CardHeader>
              <CardTitle>New Customers</CardTitle>
              <UsersIcon className="text-gray-500" />
            </CardHeader>
            <CardContent>
              <span className="font-bold text-2xl text-gray-700">{customers.toLocaleString()}</span>
              <span className={`ml-2 text-sm ${customersChange >= 0 ? "text-green-500" : "text-red-500"}`}>
                {customersChange >= 0 ? "+" : "-"}
                {Math.abs(customersChange).toFixed(2)}%
              </span>
            </CardContent>
          </Card>
          <Card>
            <CardHeader>
              <CardTitle>Average Order Value</CardTitle>
              <DollarSignIcon className="text-gray-500" />
            </CardHeader>
            <CardContent>
              <span className="font-bold text-2xl text-gray-700">${avgOrderValue.toFixed(2)}</span>
              <span className={`ml-2 text-sm ${avgOrderValueChange >= 0 ? "text-green-500" : "text-red-500"}`}>
                {avgOrderValueChange >= 0 ? "+" : "-"}
                {Math.abs(avgOrderValueChange).toFixed(2)}%
              </span>
            </CardContent>
          </Card>
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mt-8">
          <Card>
            <CardHeader>
              <CardTitle>Items Sold</CardTitle>
            </CardHeader>
            <CardContent>
              {itemsSoldData ? (
                <Line
                  data={itemsSoldData}
                  options={{
                    maintainAspectRatio: false,
                    scales: {
                      y: {
                        ticks: {
                          stepSize: 1,
                          callback: function(value) {
                            return Math.floor(value);
                          }
                        }
                      },
                      x: {
                        ticks: {
                          callback: function(value, index) {
                            return itemsSoldData.labels[index];
                          }
                        }
                      }
                    },
                    plugins: {
                      legend: {
                        display: false
                      }
                    }
                  }}
                  height={400}
                />
              ) : (
                <p>Loading...</p>
              )}
            </CardContent>
          </Card>
          <Card>
            <CardHeader>
              <CardTitle>Top Selling Categories</CardTitle>
            </CardHeader>
            <CardContent>
              {topSellingCategoriesData ? (
                <Doughnut
                  data={topSellingCategoriesData}
                  options={{
                    maintainAspectRatio: false,
                  }}
                  height={400}
                />
              ) : (
                <p>Loading...</p>
              )}
            </CardContent>
          </Card>
          <Card>
  <CardHeader>
    <CardTitle>Best Selling Products</CardTitle>
  </CardHeader>
  <CardContent>
    {bestSellingProductsData ? (
      <Bar
        data={bestSellingProductsData}
        options={{
          maintainAspectRatio: false,
          scales: {
            y: {
              ticks: {
                stepSize: 1,
                callback: function(value) {
                  return Math.floor(value);
                }
              }
            },
            x: {
              ticks: {
                autoSkip: false,
                maxRotation: 90,
                minRotation: 0,
                callback: function(value, index) {
                  return bestSellingProductsData.labels[index];
                }
              }
            }
          },
          plugins: {
            legend: {
              display: false
            }
          }
        }}
        height={400}
      />
    ) : (
      <p>Loading...</p>
    )}
  </CardContent>
</Card>

        </div>
      </main>
    </div>
  );
}

function Card({ children }) {
  return (
    <div className="bg-white border border-gray-300 rounded-lg shadow-md p-4 h-full">
      {children}
    </div>
  );
}

function CardHeader({ children }) {
  return (
    <div className="flex justify-between items-center pb-2">
      {children}
    </div>
  );
}

function CardTitle({ children }) {
  return (
    <h3 className="text-lg font-medium">{children}</h3>
  );
}

function CardContent({ children }) {
  return (
    <div className="pt-2">
      {children}
    </div>
  );
}

function DollarSignIcon(props) {
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
      <line x1="12" x2="12" y1="2" y2="22" />
      <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6" />
    </svg>
  );
}

function ShoppingCartIcon(props) {
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
      <circle cx="8" cy="21" r="1" />
      <circle cx="19" cy="21" r="1" />
      <path d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h9.78a2 2 0 0 0 1.95-1.57l1.65-7.43H5.12" />
    </svg>
  );
}

function UsersIcon(props) {
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
      <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
      <circle cx="9" cy="7" r="4" />
      <path d="M22 21v-2a4 4 0 0 0-3-3.87" />
      <path d="M16 3.13a4 4 0 0 1 0 7.75" />
    </svg>
  );
}

export default AdminDashboard;
