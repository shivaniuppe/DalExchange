import React, { createContext, useState } from 'react';

export const SearchFilterContext = createContext();

export const SearchFilterProvider = ({ children }) => {
  const [search, setSearch] = useState('');
  const [filters, setFilters] = useState({
    minPrice: -1,
    maxPrice: -1,
    categories: [],
    conditions: []
  });

  return (
    <SearchFilterContext.Provider value={{ search, setSearch, filters, setFilters }}>
      {children}
    </SearchFilterContext.Provider>
  );
};
