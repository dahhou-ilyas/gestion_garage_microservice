import { useState } from "react";
import { BACKEND_URL } from "../../config";
import axios from 'axios';


const MaintenanceScheduling = () => {
    const [maintenance, setMaintenance] = useState({
        id: null,
        startTime: '',
        endTime: '',
        description: '',
        status: 'SCHEDULED',
        vehicleId: null,
        customerId: null,
        estimatedCost: null
    });
  
    const handleMaintenanceSchedule = async (e) => {
        e.preventDefault();
        try {
          const response = await axios.post(`${BACKEND_URL}/workshop-service/api/maintenance`, {
            ...maintenance,
            startTime: new Date(maintenance.startTime).toISOString(),
            endTime: new Date(maintenance.endTime).toISOString(),
            estimatedCost: maintenance.estimatedCost ? Number(maintenance.estimatedCost) : null
          });
          alert('Maintenance scheduled successfully');
          console.log(response.data);
          // Optionally reset form or update state
          setMaintenance({
            id: null,
            startTime: '',
            endTime: '',
            description: '',
            status: 'SCHEDULED',
            vehicleId: null,
            customerId: null,
            estimatedCost: null
          });
        } catch (error) {
          console.error('Error scheduling maintenance', error);
          alert('Failed to schedule maintenance');
        }
    };

    return (
        <div className="p-4">
          <h2 className="text-2xl mb-4">Maintenance Scheduling</h2>
          <form onSubmit={handleMaintenanceSchedule} className="space-y-4">
            <div className="flex space-x-4">
              <div className="w-1/2">
                <label className="block mb-2">Start Time</label>
                <input
                  type="datetime-local"
                  value={maintenance.startTime}
                  onChange={(e) => setMaintenance({...maintenance, startTime: e.target.value})}
                  className="w-full p-2 border rounded"
                  required
                />
              </div>
              <div className="w-1/2">
                <label className="block mb-2">End Time</label>
                <input
                  type="datetime-local"
                  value={maintenance.endTime}
                  onChange={(e) => setMaintenance({...maintenance, endTime: e.target.value})}
                  className="w-full p-2 border rounded"
                  required
                />
              </div>
            </div>
            <textarea
              placeholder="Maintenance Description"
              value={maintenance.description}
              onChange={(e) => setMaintenance({...maintenance, description: e.target.value})}
              className="w-full p-2 border rounded"
              rows={4}
              required
            />
            <input
              type="number"
              placeholder="Vehicle ID"
              value={maintenance.vehicleId || ''}
              onChange={(e) => setMaintenance({...maintenance, vehicleId: Number(e.target.value)})}
              className="w-full p-2 border rounded"
              required
            />
            <input
              type="number"
              placeholder="Customer ID"
              value={maintenance.customerId || ''}
              onChange={(e) => setMaintenance({...maintenance, customerId: Number(e.target.value)})}
              className="w-full p-2 border rounded"
              required
            />
            <input
              type="number"
              placeholder="Estimated Cost"
              value={maintenance.estimatedCost || ''}
              onChange={(e) => setMaintenance({...maintenance, estimatedCost: Number(e.target.value)})}
              className="w-full p-2 border rounded"
            />
            <select
              value={maintenance.status}
              onChange={(e) => setMaintenance({...maintenance, status: e.target.value})}
              className="w-full p-2 border rounded"
            >
              <option value="SCHEDULED">Scheduled</option>
              <option value="IN_PROGRESS">In_Progress</option>
              <option value="COMPLETED">Completed</option>
            </select>
            <button 
              type="submit" 
              className="bg-purple-500 text-white p-2 rounded w-full"
            >
              Schedule Maintenance
            </button>
          </form>
        </div>
    );
};

export default MaintenanceScheduling;