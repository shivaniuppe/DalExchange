import React from 'react'

const Pagination = ({pageData, onPageChange}) => {
  const { page, totalPages } = pageData;

  const handlePageClick = (selectedPage) => {
    if (page !== selectedPage) {
      onPageChange(selectedPage);
    }
  };

  const renderPageNumbers = () => {
    const pageNumbers = [];
    for (let pageNumber = 1; pageNumber <= pageData.totalPages; pageNumber++) {
      pageNumbers.push(
        <li key={pageNumber} onClick={() => handlePageClick(pageNumber-1)} disabled={pageNumber === page}>{ 
          pageNumber-1 === page ? 
          <div aria-current="page" className="z-10 flex items-center justify-center px-3 h-8 leading-tight text-blue-600 border border-blue-300 bg-blue-50 hover:bg-blue-100 hover:text-blue-700">{pageNumber}</div>
          : <div className="flex items-center justify-center px-3 h-8 leading-tight text-gray-500 bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700">{pageNumber}</div>
        }</li>
      );
    }
    return pageNumbers;
  };

  
  return (
    <nav aria-label="Page navigation example">
      <ul className="flex items-center -space-x-px h-8 text-sm">
        <li onClick={() =>{
          if(page !== 0) {
            handlePageClick(page - 1);
          }
        }} disabled={page === 0}>
          <div href="#" className="flex items-center justify-center px-3 h-8 ms-0 leading-tight text-gray-500 bg-white border border-e-0 border-gray-300 rounded-s-lg hover:bg-gray-100 hover:text-gray-700">
            <span className="sr-only">Previous</span>
            <svg className="w-2.5 h-2.5 rtl:rotate-180" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 6 10">
              <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 1 1 5l4 4"/>
            </svg>
          </div>
        </li>
        {renderPageNumbers()}
        <li onClick={() => {
          if(page !== totalPages-1) {
            handlePageClick(page + 1);
          }
        }} disabled={page === totalPages-1}>
          <div className="flex items-center justify-center px-3 h-8 leading-tight text-gray-500 bg-white border border-gray-300 rounded-e-lg hover:bg-gray-100 hover:text-gray-700">
            <span className="sr-only">Next</span>
            <svg className="w-2.5 h-2.5 rtl:rotate-180" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 6 10">
              <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m1 9 4-4-4-4"/>
            </svg>
          </div>
        </li>
      </ul>
    </nav>
  )
}

export default Pagination