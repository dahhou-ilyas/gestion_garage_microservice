import { useState } from "react";
import CustomerManagement from "./component/CustomerManagement";
import VehicleManagement from "./component/VehicleManagement.JSX";
import MaintenanceScheduling from "./component/MaintenanceScheduling";

function App() {

  const [activeTab, setActiveTab] = useState('customers');

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
