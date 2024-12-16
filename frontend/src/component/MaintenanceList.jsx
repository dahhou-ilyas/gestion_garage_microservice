import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BACKEND_URL } from '../../config';

const MaintenanceList = () => {
  const [maintenances, setMaintenances] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filter, setFilter] = useState('ALL');

  useEffect(() => {
    const fetchMaintenances = async () => {
      try {
        // Ensure the endpoint matches the backend controller
        const response = await axios.get(`${BACKEND_URL}/workshop-service/api/maintenance`);
        setMaintenances(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch maintenance records');
        setLoading(false);
      }
    };
    fetchMaintenances();
  }, []);

  const filteredMaintenances = maintenances.filter(maintenance =>
    filter === 'ALL' || maintenance.status === filter
  );

  if (loading) return <div>Loading maintenance records...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="p-4">
      <h2 className="text-2xl mb-4">Maintenance Records</h2>
      <div className="mb-4">
        <label className="mr-2">Filter by Status:</label>
        <select
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
          className="p-2 border rounded"
        >
          <option value="ALL">All Maintenance</option>
          <option value="SCHEDULED">Scheduled</option>
          <option value="IN_PROGRESS">In Progress</option>
          <option value="COMPLETED">Completed</option>
        </select>
      </div>
      <table className="w-full border-collapse border">
        <thead>
          <tr className="bg-gray-200">
            <th className="border p-2">Vehicle</th>
            <th className="border p-2">Customer</th>
            <th className="border p-2">Start Time</th>
            <th className="border p-2">End Time</th>
            <th className="border p-2">Description</th>
            <th className="border p-2">Status</th>
            <th className="border p-2">Estimated Cost</th>
          </tr>
        </thead>
        <tbody>
          {filteredMaintenances.map((maintenance) => (
            <tr key={maintenance.id} className="hover:bg-gray-100">
              <td className="border p-2">
                {maintenance.vehicle ? 
                  `${maintenance.vehicle.brand} ${maintenance.vehicle.model}` : 
                  maintenance.vehicleId
                }
              </td>
              <td className="border p-2">
                {maintenance.customer ? 
                  `${maintenance.customer.firstName} ${maintenance.customer.lastName}` : 
                  maintenance.customerId
                }
              </td>
              <td className="border p-2">{new Date(maintenance.startTime).toLocaleString()}</td>
              <td className="border p-2">{new Date(maintenance.endTime).toLocaleString()}</td>
              <td className="border p-2">{maintenance.description}</td>
              <td className="border p-2">{maintenance.status}</td>
              <td className="border p-2">
                {maintenance.estimatedCost ? 
                  `$${maintenance.estimatedCost.toFixed(2)}` : 'N/A'}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MaintenanceList;