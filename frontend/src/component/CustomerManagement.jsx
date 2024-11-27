import React, { useState } from 'react';
import axios from 'axios';
import { BACKEND_URL } from '../../config';

// API Base URL (adjust according to your API Gateway configuration)


// Customer Management Component
const CustomerManagement = () => {
  const [customer, setCustomer] = useState({
    identityNumber: '',
    firstName: '',
    lastName: '',
    address: '',
    phone: '',
    email: ''
  });

  const handleCustomerSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(`${BACKEND_URL}/customers`, customer);
      alert('Customer created successfully');
      console.log(response.data);
    } catch (error) {
      console.error('Error creating customer', error);
    }
  };

  return (
    <div className="p-4">
      <h2 className="text-2xl mb-4">Customer Management</h2>
      <form onSubmit={handleCustomerSubmit} className="space-y-4">
        <input
          type="text"
          placeholder="Identity Number"
          value={customer.identityNumber}
          onChange={(e) => setCustomer({...customer, identityNumber: e.target.value})}
          className="w-full p-2 border rounded"
        />
        <input
          type="text"
          placeholder="First Name"
          value={customer.firstName}
          onChange={(e) => setCustomer({...customer, firstName: e.target.value})}
          className="w-full p-2 border rounded"
        />
        {/* Add other input fields similarly */}
        <button type="submit" className="bg-blue-500 text-white p-2 rounded">
          Create Customer
        </button>
      </form>
    </div>
  );
};


export default CustomerManagement;