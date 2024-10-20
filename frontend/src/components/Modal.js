import React from 'react';

const Modal = ({title, body, acceptCTA, rejectCTA, isNegative}) => {

  return (
    <div id="popup-modal" tabIndex="-1" className="overflow-y-auto overflow-x-hidden fixed z-50 inset-0 bg-gray-800 bg-opacity-75 flex items-center justify-center">
      <div className="relative p-4 w-full max-w-md max-h-full">
        <div className="relative bg-white rounded-lg shadow">
          <button type="button" className="absolute top-3 end-2.5 text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center" data-modal-hide="popup-modal">
            <svg className="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
            </svg>
            <span className="sr-only">Close modal</span>
          </button>
          <div className="p-4 md:p-5 text-center">
          <h3 className="mb-5 text-lg font-semibold text-gray-900">{title}</h3>
            <h3 className="mb-5 text-lg font-normal text-gray-700">{body}</h3>
            {isNegative && <button data-modal-hide="popup-modal" type="button" className="text-white bg-red-600 hover:bg-red-800 focus:ring-4 focus:outline-none font-medium rounded-lg text-sm inline-flex items-center px-5 py-2.5 text-center">
              {acceptCTA}
            </button>}
            {!isNegative && <button data-modal-hide="popup-modal" type="button" className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 focus:outline-none">
              {acceptCTA}
            </button>}
            <button data-modal-hide="popup-modal" type="button" className="py-2.5 px-5 ms-3 text-sm font-medium text-gray-900 focus:outline-none bg-white rounded-lg border border-gray-200 hover:bg-gray-100 hover:text-blue-700 focus:z-10">
              {rejectCTA}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Modal;