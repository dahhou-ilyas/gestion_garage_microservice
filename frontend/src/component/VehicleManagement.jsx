import { useState } from "react";
import { BACKEND_URL } from "../../config";
import axios from 'axios';


const VehicleManagement = () => {
    const [vehicle, setVehicle] = useState({
      chassisNumber: '',
      registrationNumber: '',
      brand: '',
      model: '',
      year: '',
      color: '',
      mileage: '',
      fuelType: '',
      purchaseDate: '',
      ownerId: '',
      status: ''
    });
  
    const handleVehicleSubmit = async (e) => {
      e.preventDefault();
      try {
        const response = await axios.post(`${BACKEND_URL}/cars`, vehicle);
        alert('Vehicle added successfully');
        console.log(response.data);
      } catch (error) {
        console.error('Error adding vehicle', error);
      }
    };
  
    return (
      <div className="p-4">
        <h2 className="text-2xl mb-4">Vehicle Management</h2>
        <form onSubmit={handleVehicleSubmit} className="space-y-4">
          <input
            type="text"
            placeholder="Chassis Number"
            value={vehicle.chassisNumber}
            onChange={(e) => setVehicle({...vehicle, chassisNumber: e.target.value})}
            className="w-full p-2 border rounded"
          />
          <input
            type="text"
            placeholder="Registration Number"
            value={vehicle.registrationNumber}
            onChange={(e) => setVehicle({...vehicle, registrationNumber: e.target.value})}
            className="w-full p-2 border rounded"
          />
          {/* Add other input fields similarly */}
          <button type="submit" className="bg-green-500 text-white p-2 rounded">
            Add Vehicle
          </button>
        </form>
      </div>
    );
};

export default VehicleManagement;