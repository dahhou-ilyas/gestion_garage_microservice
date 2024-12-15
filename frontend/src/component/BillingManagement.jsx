import React, { useState } from 'react';
import axios from 'axios';
import { BACKEND_URL } from '../../config';

const BillingManagement = () => {
  const [billing, setBilling] = useState({
    id: null,
    maintenanceId: null,
    customerId: null,
    totalAmount: 0,
    paymentStatus: 'PENDING',
    billingDate: new Date().toISOString().split('T')[0],
    dueDate: '',
    additionalCharges: 0,
    discounts: 0
  });

  const handleBillingSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(`${BACKEND_URL}/billing`, {
        ...billing,
        totalAmount: Number(billing.totalAmount),
        additionalCharges: Number(billing.additionalCharges),
        discounts: Number(billing.discounts)
      });
      
      alert('Billing created successfully');
      console.log(response.data);
      
      // Reset form
      setBilling({
        id: null,
        maintenanceId: null,
        customerId: null,
        totalAmount: 0,
        paymentStatus: 'PENDING',
        billingDate: new Date().toISOString().split('T')[0],
        dueDate: '',
        additionalCharges: 0,
        discounts: 0
      });
    } catch (error) {
      console.error('Error creating billing', error);
      alert('Failed to create billing');
    }
  };

  return (
    <div className="p-4">
      <h2 className="text-2xl mb-4">Billing Management</h2>
      <form onSubmit={handleBillingSubmit} className="space-y-4">
        <input
          type="number"
          placeholder="Maintenance ID"
          value={billing.maintenanceId || ''}
          onChange={(e) => setBilling({...billing, maintenanceId: Number(e.target.value)})}
          className="w-full p-2 border rounded"
          required
        />
        <input
          type="number"
          placeholder="Customer ID"
          value={billing.customerId || ''}
          onChange={(e) => setBilling({...billing, customerId: Number(e.target.value)})}
          className="w-full p-2 border rounded"
          required
        />
        <input
          type="number"
          placeholder="Total Amount"
          value={billing.totalAmount}
          onChange={(e) => setBilling({...billing, totalAmount: Number(e.target.value)})}
          className="w-full p-2 border rounded"
          required
          step="0.01"
        />
        <input
          type="number"
          placeholder="Additional Charges"
          value={billing.additionalCharges}
          onChange={(e) => setBilling({...billing, additionalCharges: Number(e.target.value)})}
          className="w-full p-2 border rounded"
          step="0.01"
        />
        <input
          type="number"
          placeholder="Discounts"
          value={billing.discounts}
          onChange={(e) => setBilling({...billing, discounts: Number(e.target.value)})}
          className="w-full p-2 border rounded"
          step="0.01"
        />
        <input
          type="date"
          placeholder="Billing Date"
          value={billing.billingDate}
          onChange={(e) => setBilling({...billing, billingDate: e.target.value})}
          className="w-full p-2 border rounded"
          required
        />
        <input
          type="date"
          placeholder="Due Date"
          value={billing.dueDate}
          onChange={(e) => setBilling({...billing, dueDate: e.target.value})}
          className="w-full p-2 border rounded"
          required
        />
        <select
          value={billing.paymentStatus}
          onChange={(e) => setBilling({...billing, paymentStatus: e.target.value})}
          className="w-full p-2 border rounded"
        >
          <option value="PENDING">Pending</option>
          <option value="PAID">Paid</option>
          <option value="OVERDUE">Overdue</option>
        </select>
        <button 
          type="submit" 
          className="bg-green-500 text-white p-2 rounded w-full"
        >
          Create Billing
        </button>
      </form>
    </div>
  );
};

export default BillingManagement;