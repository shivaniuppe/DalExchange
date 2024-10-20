import React, { useContext, useEffect, useState } from 'react';
import { ProductListingAPI } from '../../../services/productListingApi';
import { SearchFilterContext } from '../../../context/SearchFilterContext';

const Sidebar = ({ onFilterSubmit }) => {
  const { filters, setFilters } = useContext(SearchFilterContext);
  const [categories, setCategories] = useState([]);
  const productConditions = [ "New", "Good", "Fair", "Used", "Poor"];

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    await ProductListingAPI.getCategories({ categories: setCategories });
  };

  const setFilter = (key, value) => {
    setFilters({ ...filters, [key]: value });
  };

  const setFilterArray = (key, checked, value) => {
    if (checked) {
      setFilters({ ...filters, [key]: [...filters[key], value] });
    } else {
      const filterArray = filters[key];
      const index = filterArray.indexOf(value);
      if (index > -1) {
        filterArray.splice(index, 1);
      }
      setFilters({ ...filters, [key]: filterArray });
    }
  };

  const applyFilters = () => {
    onFilterSubmit();
  };

  return (
    <div>
      <button
        data-drawer-target='default-sidebar'
        data-drawer-toggle='default-sidebar'
        aria-controls='default-sidebar'
        type='button'
        className='inline-flex items-center p-2 mt-2 ms-3 text-sm text-gray-500 rounded-lg sm:hidden hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200'
      >
        <span className='sr-only'>Open sidebar</span>
        <svg
          className='w-6 h-6'
          aria-hidden='true'
          fill='currentColor'
          viewBox='0 0 20 20'
          xmlns='http://www.w3.org/2000/svg'
        >
          <path
            clipRule='evenodd'
            fillRule='evenodd'
            d='M2 4.75A.75.75 0 012.75 4h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 4.75zm0 10.5a.75.75 0 01.75-.75h7.5a.75.75 0 010 1.5h-7.5a.75.75 0 01-.75-.75zM2 10a.75.75 0 01.75-.75h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 10z'
          ></path>
        </svg>
      </button>
      <aside
        id='default-sidebar'
        className='bg-gray-100 p-6 border-r w-64 transition-transform -translate-x-full sm:translate-x-0 h-full'
      >
        <h2 className='text-lg font-medium mb-4'>Filters</h2>
        <div className='space-y-4'>
          <div>
            <h3 className='text-base font-medium mb-2'>Price</h3>
            <div className='flex items-center gap-2'>
              <input
                className='bg-white rounded-md px-3 py-2 text-sm w-24 focus:outline-none focus:ring-2 focus:ring-gray-600'
                placeholder='Min'
                type='number'
                value={filters?.minPrice > 0 ? filters.minPrice : ''}
                onChange={(event) => setFilter('minPrice', event.target.value)}
              />
              <span>-</span>
              <input
                className='bg-white rounded-md px-3 py-2 text-sm w-24 focus:outline-none focus:ring-2 focus:ring-gray-600'
                placeholder='Max'
                type='number'
                value={filters?.maxPrice > 0 ? filters.maxPrice : ''}
                onChange={(event) => setFilter('maxPrice', event.target.value)}
              />
            </div>
          </div>
          <div>
            <h3 className='text-base font-medium mb-2'>Category</h3>
            <div className='space-y-2'>
              {categories &&
                categories.map((category) => (
                  <div className='flex items-center gap-2' key={category}>
                    <input
                      id='category-furniture'
                      type='checkbox'
                      value=''
                      className='w-4 h-4 text-blue-600 accent-black bg-gray-100 border-gray-300 rounded'
                      defaultChecked={filters?.categories?.includes(category)}
                      onChange={(event) =>
                        setFilterArray('categories', event.target.checked, category)
                      }
                    />
                    <label
                      className='text-sm font-medium'
                      htmlFor='category-furniture'
                    >
                      {category}
                    </label>
                  </div>
                ))}
            </div>
          </div>
          <div>
            <h3 className='text-base font-medium mb-2'>Condition</h3>
            <div className='space-y-2'>
              {productConditions &&
                productConditions.map((productCondition) => (
                  <div className='flex items-center gap-2' key={productCondition}>
                    <input
                      id='condition-new'
                      type='checkbox'
                      value=''
                      className='w-4 h-4 text-blue-600 accent-black bg-gray-100 border-gray-300 rounded'
                      defaultChecked={filters?.conditions?.includes(productCondition)}
                      onChange={(event) =>
                        setFilterArray('conditions', event.target.checked, productCondition)
                      }
                    />
                    <label className='text-sm font-medium' htmlFor='condition-new'>
                      {productCondition}
                    </label>
                  </div>
                ))}
            </div>
          </div>
          <button
            type='button'
            className='text-white bg-black hover:bg-gray-800 focus:outline-none font-large font-semibold rounded-lg text-sm py-2 w-full tracking-wide'
            onClick={applyFilters}
          >
            Apply Filters
          </button>
        </div>
      </aside>
    </div>
  );
};

export default Sidebar;
