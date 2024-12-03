import { useEffect, useState } from "react";
import CustomerManagement from "./component/CustomerManagement";
import VehicleManagement from "./component/VehicleManagement.JSX";
import MaintenanceScheduling from "./component/MaintenanceScheduling";
import Authentication from "./component/Authentication";
import axios from "axios";
import { BACKEND_URL } from "../config";


function App() {

  const [activeTab, setActiveTab] = useState('customers');
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    // Check for existing authentication token on app load
    const token = localStorage.getItem('authToken');
    if (token) {
      // Verify token with backend (optional but recommended)
      verifyToken(token);
    }
  }, []);

  const verifyToken = async (token) => {
    try {
      // Implement token verification endpoint
      await axios.get(`${BACKEND_URL}/auth/verify`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setIsAuthenticated(true);
      
      // Set the Authorization header for future requests
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } catch (error) {
      // Token is invalid
      localStorage.removeItem('authToken');
      setIsAuthenticated(false);
    }
  };

  const handleLoginSuccess = () => {
    setIsAuthenticated(true);
  };

  const handleLogout = () => {
    // Remove token from localStorage
    localStorage.removeItem('authToken');
    
    // Remove Authorization header
    delete axios.defaults.headers.common['Authorization'];
    
    // Set authentication state to false
    setIsAuthenticated(false);
  };

  if (!isAuthenticated) {
    return <Authentication onLoginSuccess={handleLoginSuccess} />;
  }

  return (
    <div className="container mx-auto">
      <h1 className="text-3xl font-bold text-center my-6">Garage Management System</h1>
      
      <div className="flex justify-center mb-6">
        <button 
          onClick={() => setActiveTab('customers')}
          className={`mx-2 px-4 py-2 rounded ${activeTab === 'customers' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
        >
          Customers
        </button>
        <button 
          onClick={() => setActiveTab('vehicles')}
          className={`mx-2 px-4 py-2 rounded ${activeTab === 'vehicles' ? 'bg-green-500 text-white' : 'bg-gray-200'}`}
        >
          Vehicles
        </button>
        <button 
          onClick={() => setActiveTab('maintenance')}
          className={`mx-2 px-4 py-2 rounded ${activeTab === 'maintenance' ? 'bg-purple-500 text-white' : 'bg-gray-200'}`}
        >
          Maintenance
        </button>
      </div>

      {activeTab === 'customers' && <CustomerManagement />}
      {activeTab === 'vehicles' && <VehicleManagement />}
      {activeTab === 'maintenance' && <MaintenanceScheduling />}
    </div>
  );
}

export default App
