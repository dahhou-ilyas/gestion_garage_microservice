import { useState } from "react";
import { BACKEND_URL } from "../../config";
import axios from 'axios';


const MaintenanceScheduling = () => {
    const [maintenance, setMaintenance] = useState({
      startTime: '',
      endTime: '',
      description: '',
      status: '',
      vehicleId: ''
    });
  
    const handleMaintenanceSchedule = async (e) => {
      e.preventDefault();
      try {
        const response = await axios.post(`${BACKEND_URL}/maintenance`, maintenance);
        alert('Maintenance scheduled successfully');
        console.log(response.data);
      } catch (error) {
        console.error('Error scheduling maintenance', error);
      }
    };
  
    return (
      <div className="p-4">
        <h2 className="text-2xl mb-4">Maintenance Scheduling</h2>
        <form onSubmit={handleMaintenanceSchedule} className="space-y-4">
          <input
            type="datetime-local"
            placeholder="Start Time"
            value={maintenance.startTime}
            onChange={(e) => setMaintenance({...maintenance, startTime: e.target.value})}
            className="w-full p-2 border rounded"
          />
          <input
            type="datetime-local"
            placeholder="End Time"
            value={maintenance.endTime}
            onChange={(e) => setMaintenance({...maintenance, endTime: e.target.value})}
            className="w-full p-2 border rounded"
          />
          {/* Add other input fields similarly */}
          <button type="submit" className="bg-purple-500 text-white p-2 rounded">
            Schedule Maintenance
          </button>
        </form>
      </div>
    );
};

export default MaintenanceScheduling;