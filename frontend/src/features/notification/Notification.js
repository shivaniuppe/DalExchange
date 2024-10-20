import React, { useEffect, useState } from 'react'
import { NotificationApi } from '../../services/NotificationApi';
import Loader from '../../components/Loader';
import ErrorAlert from '../../components/ErrorAlert';

const Notification = () => {

  const [notifications, setNotifications] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchNotifications = async () => {
      const setters = {
        notifications: setNotifications,
        isLoading: setIsLoading,
        error: setError
      };
      await NotificationApi.get(setters);
    };
    fetchNotifications();
  }, []);

  const markNotificationRead = async (id) => {
    await NotificationApi.mark(id).then(() => {
      const updatedNotifications = notifications.map((notification) => {
        if (notification.id === id) {
          return { ...notification, isRead: true }
        }
        return notification;
      });
      setNotifications(updatedNotifications);
    });
  }

  return (
    <div className="fixed w-full max-w-md z-50 top-20 right-32 block max-w-sm pt-6 pb-2 pl-6 bg-white border border-gray-200 rounded-lg shadow">
      <div className="mb-2 text-2xl font-bold tracking-tight text-gray-900">
        Notifications
      </div>
      <div className='pr-6'>
        {isLoading && <Loader title={'Loading Notifications...'} />}
        {!isLoading && error && <ErrorAlert message={error.message} />} 

        {!isLoading && !error && notifications && notifications.length === 0 && 
          <div className="flex justify-center h-16 w-full" >
            <div className="p-3 px-12 mt-4 text-sm font-medium text-gray-800 rounded-lg bg-gray-50 border-2 border-gray-800" role="alert">
              You have recived no notifcations yet.
            </div>
          </div>
        }
      </div>
      {
        !isLoading && !error && notifications && notifications.length > 0 &&
        <div className="relative font-normal max-h-[calc(100vh-180px)] overflow-y-auto pb-4">
          <div className="space-y-4">
            
            {notifications.map((notification) => (
              notification.isRead ? 
              (<div key={notification.id} className="flex items-start gap-4 rounded-md text-gray-700 bg-white border border-gray-200 drop-shadow-md p-4 mr-6 transition-colors hover:bg-gray-100">
                <div className="space-y-1">
                  <div className="flex items-center justify-between">
                    <p className="font-medium">{notification.title}</p>
                    <p className="text-xs text-muted-foreground">{notification.createdAt[2]}/{notification.createdAt[1]}/{notification.createdAt[0]}</p>
                  </div>
                  <p className="text-sm text-muted-foreground">
                    {notification.message}
                  </p>
                </div>
              </div>) : 
              (<div key={notification.id} 
                className="flex items-start gap-4 rounded-md text-gray-700 bg-sky-50 border border-sky-100 drop-shadow-md p-4 mr-6 transition-colors hover:bg-sky-100"
                onClick={() => markNotificationRead(notification.id)}>
                <div className="space-y-1">
                  <div className="flex items-center justify-between">
                    <p className="font-medium ">{notification.title}</p>
                    <p className="text-xs text-muted-foreground">{notification.createdAt[2]}/{notification.createdAt[1]}/{notification.createdAt[0]}</p>
                  </div>
                  <p className="text-sm text-muted-foreground">
                    {notification.message}
                  </p>
                </div>
              </div>)
            ))}

          </div>
        </div>
      }
    </div>
  )
}

export default Notification