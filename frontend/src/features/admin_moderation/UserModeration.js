import { useState, useEffect } from "react";
import UserModerationApi from "../../services/UserModerationApi";

export default function UserModeration() {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const users = await UserModerationApi.fetchUsers();
        setUsers(users);
      } catch (error) {
        console.error("There was an error fetching the users!", error);
      }
    };

    fetchUsers();
  }, []);

  const handleUserClick = async (userId) => {
    try {
      const userDetails = await UserModerationApi.fetchUserDetails(userId);
      setSelectedUser(userDetails);
    } catch (error) {
      console.error("There was an error fetching the user details!", error);
    }
  };

  const handleSaveUser = async (updatedUser) => {
    try {
      await UserModerationApi.updateUser(updatedUser.userId, updatedUser);
      setUsers((prevUsers) => prevUsers.map((user) => (user.userId === updatedUser.userId ? updatedUser : user)));
      setSelectedUser(null);
    } catch (error) {
      console.error("There was an error updating the user!", error);
    }
  };

  const handleResetPassword = async (email) => {
    try {
      const message = await UserModerationApi.resetPassword(email);
      alert(message); 
    } catch (error) {
      console.error("There was an error requesting the password reset!", email);
      alert("There was an error requesting the password reset.");
    }
  };

  const handleActivateDeactivate = async (userId) => {
    try {
      const user = users.find((user) => user.userId === userId);
      await UserModerationApi.toggleUserStatus(userId, !user.active);
      setUsers((prevUsers) => prevUsers.map((u) => (u.userId === userId ? { ...u, active: !u.active } : u)));
      setSelectedUser((prevUser) => ({ ...prevUser, active: !prevUser.active }));
    } catch (error) {
      console.error("There was an error activating/deactivating the user!", error);
    }
  };

  const handleDeleteUser = async (userId) => {
    try {
      await UserModerationApi.deleteUser(userId);
      setUsers((prevUsers) => prevUsers.filter((user) => user.userId !== userId));
      setSelectedUser(null);
    } catch (error) {
      console.error("There was an error deleting the user!", error);
    }
  };

  return (
    <div className="flex flex-col min-h-screen w-full">
      <header className="flex h-14 lg:h-[60px] items-center gap-4 border-b bg-gray-200 px-6 mx-4 mt-4 rounded-lg">
        <div className="w-full flex justify-center">
          <h1 className="text-3xl font-bold">User Moderation</h1>
        </div>
      </header>
      <main className="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-6">
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {users.map((user) => (
            <div
              key={user.userId}
              onClick={() => handleUserClick(user.userId)}
              className="cursor-pointer border border-gray-300 rounded-lg p-4 hover:bg-gray-100 transition-colors"
            >
              <div className="mb-2">
                <div className="text-xl font-bold underline">{user.fullName}</div>
                <div className="text-gray-500">{user.role === "admin" ? "Administrator" : "Student"}</div>
              </div>
              <div className="grid gap-2">
                <div className="flex items-center justify-between">
                  <span className="text-black font-bold">Email:</span>
                  <span>{user.email}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-black font-bold">Phone:</span>
                  <span>{user.phoneNo}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-black font-bold">Status:</span>
                  <span className={`px-2 py-1 rounded-full ${user.active ? "bg-green-500 text-white" : "bg-red-500 text-white"}`}>
                    {user.active ? "Active" : "Inactive"}
                  </span>
                </div>
              </div>
            </div>
          ))}
        </div>
        {selectedUser && (
          <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
            <div className="bg-white p-6 rounded-lg w-full max-w-md">
              <div className="flex flex-col gap-6">
                <div className="grid gap-2">
                  <label htmlFor="fullName" className="font-bold">Full Name</label>
                  <input
                    id="fullName"
                    className="border border-gray-300 rounded p-2"
                    defaultValue={selectedUser.fullName}
                    onChange={(e) => setSelectedUser({ ...selectedUser, fullName: e.target.value })}
                  />
                </div>
                <div className="grid gap-2">
                  <label htmlFor="email" className="font-bold">Email</label>
                  <input
                    id="email"
                    type="email"
                    className="border border-gray-300 rounded p-2"
                    defaultValue={selectedUser.email}
                    onChange={(e) => setSelectedUser({ ...selectedUser, email: e.target.value })}
                  />
                </div>
                <div className="grid gap-2">
                  <label htmlFor="phoneNo" className="font-bold">Phone Number</label>
                  <input
                    id="phoneNo"
                    className="border border-gray-300 rounded p-2"
                    defaultValue={selectedUser.phoneNo}
                    onChange={(e) => setSelectedUser({ ...selectedUser, phoneNo: e.target.value })}
                  />
                </div>
                <div className="grid gap-2">
                  <label htmlFor="role" className="font-bold">Role</label>
                  <select
                    id="role"
                    className="border border-gray-300 rounded p-2"
                    defaultValue={selectedUser.role}
                    onChange={(e) => setSelectedUser({ ...selectedUser, role: e.target.value })}
                  >
                    <option value="admin">Administrator</option>
                    <option value="student">Student</option>
                  </select>
                </div>
                <div className="flex items-center justify-between">
                  <span
                    className="text-black cursor-pointer underline"
                    onClick={() => handleResetPassword(selectedUser.email)}
                  >
                    Reset Password
                  </span>
                  <div className="flex gap-2">
                    <button
                      className="px-4 py-2 bg-red-500 text-white rounded"
                      onClick={() => handleDeleteUser(selectedUser.userId)}
                    >
                      Delete User
                    </button>
                    <button
                      className={`px-4 py-2 ${selectedUser.active ? "bg-red-500" : "bg-green-500"} text-white rounded`}
                      onClick={() => handleActivateDeactivate(selectedUser.userId)}
                    >
                      {selectedUser.active ? "Deactivate" : "Activate"}
                    </button>
                  </div>
                </div>
                <div className="flex justify-between gap-2">
                  <button className="px-6 py-2 w-full border border-gray-300 rounded" onClick={() => setSelectedUser(null)}>
                    Cancel
                  </button>
                  <button className="px-6 py-2 w-full bg-black text-white rounded" onClick={() => handleSaveUser(selectedUser)}>
                    Save
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
}
