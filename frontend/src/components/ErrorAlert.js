import React from 'react';

const ErrorAlert = ({message}) => {
  return (
    <div className="flex justify-center h-16 w-full" >
      <div className="flex items-center py-4 px-12 mt-4 text-sm text-red-600 rounded-lg bg-red-50 border-2 border-red-600" role="alert">
        <span className="sr-only">Error</span>
        <div>
          <span className="font-medium">Error!</span> {message}
        </div>
      </div>
    </div>
  );
}

export default ErrorAlert;