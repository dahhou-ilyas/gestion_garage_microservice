import { useState } from 'react';
import axios from 'axios';
import { BACKEND_URL } from '../../config';

// API Base URL (adjust according to your API Gateway configuration)


// Customer Management Component
const CustomerManagement = () => {
    const [customer, setCustomer] = useState({
        id: null,
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
      // Optionally reset form or update state
      setCustomer({
        id: null,
        identityNumber: '',
        firstName: '',
        lastName: '',
        address: '',
        phone: '',
        email: ''
      });
    } catch (error) {
      console.error('Error creating customer', error);
      alert('Failed to create customer');
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
          required
        />
        <input
          type="text"
          placeholder="First Name"
          value={customer.firstName}
          onChange={(e) => setCustomer({...customer, firstName: e.target.value})}
          className="w-full p-2 border rounded"
          required
        />
        <input
          type="text"
          placeholder="Last Name"
          value={customer.lastName}
          onChange={(e) => setCustomer({...customer, lastName: e.target.value})}
          className="w-full p-2 border rounded"
          required
        />
        <input
          type="text"
          placeholder="Address"
          value={customer.address}
          onChange={(e) => setCustomer({...customer, address: e.target.value})}
          className="w-full p-2 border rounded"
        />
        <input
          type="tel"
          placeholder="Phone"
          value={customer.phone}
          onChange={(e) => setCustomer({...customer, phone: e.target.value})}
          className="w-full p-2 border rounded"
          required
        />
        <input
          type="email"
          placeholder="Email"
          value={customer.email}
          onChange={(e) => setCustomer({...customer, email: e.target.value})}
          className="w-full p-2 border rounded"
          required
        />
        <button 
          type="submit" 
          className="bg-blue-500 text-white p-2 rounded w-full"
        >
          Create Customer
        </button>
      </form>
    </div>
  );
};


export default CustomerManagement;