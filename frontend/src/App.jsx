import { useEffect, useState } from "react";
import { 
  UserIcon, 
  CarIcon, 
  WrenchIcon, 
  ClipboardListIcon, 
  DollarSignIcon, 
  LogOutIcon 
} from 'lucide-react';
import CustomerManagement from "./component/CustomerManagement";
import VehicleManagement from "./component/VehicleManagement";
import MaintenanceScheduling from "./component/MaintenanceScheduling";
import Authentication from "./component/Authentication";
import CustomerList from "./component/CustomerList";
import VehicleList from "./component/VehicleList";
import MaintenanceList from "./component/MaintenanceList";
import BillingManagement from "./component/BillingManagement";
import axios from "axios";
import { BACKEND_URL } from "../config";

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [activeSubTab, setActiveSubTab] = useState('list');
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    if (token) {
      setIsAuthenticated(true);
    }
  }, []);


  const handleLoginSuccess = (userData) => {
    setIsAuthenticated(true);
    setUserInfo(userData);
  };

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    delete axios.defaults.headers.common['Authorization'];
    setIsAuthenticated(false);
    setUserInfo(null);
  };

  if (!isAuthenticated) {
     return <Authentication onLoginSuccess={handleLoginSuccess} />;
  }

  const NavButton = ({ icon: Icon, label, tabName, subTab = 'list' }) => (
    <button
      onClick={() => {
        setActiveTab(tabName);
        setActiveSubTab(subTab);
      }}
      className={`flex items-center justify-center p-3 rounded-lg transition-all duration-300 
        ${activeTab === tabName 
          ? 'bg-indigo-600 text-white shadow-lg' 
          : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}
    >
      <Icon className="mr-2" />
      <span className="hidden md:inline">{label}</span>
    </button>
  );

  const renderContent = () => {
    switch (activeTab) {
      case 'customers':
        return activeSubTab === 'list' 
          ? <CustomerList /> 
          : <CustomerManagement />;
      
      case 'vehicles':
        return activeSubTab === 'list' 
          ? <VehicleList /> 
          : <VehicleManagement />;
      
      case 'maintenance':
        return activeSubTab === 'list' 
          ? <MaintenanceList /> 
          : <MaintenanceScheduling />;
      
      case 'billing':
        return activeSubTab === 'list' 
          ? null  // You might want to add a billing list component
          : <BillingManagement />;
      
      default:
        return <Dashboard userInfo={userInfo} />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      {/* Header */}
      <header className="bg-white shadow-md p-4 flex justify-between items-center">
        <h1 className="text-2xl font-bold text-indigo-600">
          Garage Management System
        </h1>
        <div className="flex items-center space-x-4">
          <span className="text-gray-700">
            Welcome, {userInfo?.firstName || 'User'}
          </span>
          <button 
            onClick={handleLogout}
            className="bg-red-500 text-white p-2 rounded-full hover:bg-red-600 transition"
          >
            <LogOutIcon size={20} />
          </button>
        </div>
      </header>

      {/* Main Content */}
      <div className="flex flex-1">
        {/* Sidebar Navigation */}
        <nav className="bg-white w-20 md:w-64 p-4 shadow-md flex flex-col space-y-4">
          <NavButton icon={UserIcon} label="Customers" tabName="customers" />
          <NavButton icon={CarIcon} label="Vehicles" tabName="vehicles" />
          <NavButton icon={WrenchIcon} label="Maintenance" tabName="maintenance" />
          <NavButton icon={DollarSignIcon} label="Billing" tabName="billing" />
        </nav>

        {/* Content Area */}
        <main className="flex-1 p-6 bg-gray-50 overflow-auto">
          {/* Tabs for List/Management */}
          <div className="flex mb-4 space-x-4">
            {['customers', 'vehicles', 'maintenance', 'billing'].includes(activeTab) && (
              <>
                <button
                  onClick={() => setActiveSubTab('list')}
                  className={`px-4 py-2 rounded ${
                    activeSubTab === 'list' 
                      ? 'bg-indigo-600 text-white' 
                      : 'bg-gray-200 text-gray-700'
                  }`}
                >
                  <ClipboardListIcon className="inline mr-2" /> View List
                </button>
                <button
                  onClick={() => setActiveSubTab('manage')}
                  className={`px-4 py-2 rounded ${
                    activeSubTab === 'manage' 
                      ? 'bg-green-600 text-white' 
                      : 'bg-gray-200 text-gray-700'
                  }`}
                >
                  <WrenchIcon className="inline mr-2" /> Manage
                </button>
              </>
            )}
          </div>

          {renderContent()}
        </main>
      </div>
    </div>
  );
}

// Simple Dashboard Component
const Dashboard = ({ userInfo }) => {
  useEffect(()=>{
    
  })
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <DashboardCard 
        title="Total Customers" 
        value="124" 
        icon={<UserIcon className="text-blue-500" />} 
      />
      <DashboardCard 
        title="Total Vehicles" 
        value="87" 
        icon={<CarIcon className="text-green-500" />} 
      />
      <DashboardCard 
        title="Active Maintenance" 
        value="12" 
        icon={<WrenchIcon className="text-purple-500" />} 
      />
      <DashboardCard 
        title="Pending Billing" 
        value="5" 
        icon={<DollarSignIcon className="text-red-500" />} 
      />
    </div>
  );
};

// Dashboard Card Component
const DashboardCard = ({ title, value, icon }) => {
  return (
    <div className="bg-white p-6 rounded-lg shadow-md flex items-center justify-between">
      <div>
        <h3 className="text-gray-500 text-sm">{title}</h3>
        <p className="text-2xl font-bold">{value}</p>
      </div>
      {icon}
    </div>
  );
};

export default App;