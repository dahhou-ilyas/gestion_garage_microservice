import { useState } from "react";
import { BACKEND_URL } from "../../config";
import axios from 'axios';


const VehicleManagement = () => {
    const [vehicle, setVehicle] = useState({
        id: null ,
        regestrationNumber: '',
        marque: '',
        model: '',
        yearOfFabrication: 0,
        colore: '',
        km: 0,
        typCarburant: '',
        dateAchat: '' ,
        idOwner: null,
        etatCars: ''
    });

    const handleVehicleSubmit = async (e) => {
        e.preventDefault();
        try {
          const response = await axios.post(`${BACKEND_URL}/cars-service/api/cars`, {
            ...vehicle,
            yearOfFabrication: Number(vehicle.yearOfFabrication),
            km: Number(vehicle.km)
          });
          alert('Vehicle added successfully');
          console.log(response.data);
          // Optionally reset form or update state
          setVehicle({
            id: null,
            regestrationNumber: '',
            marque: '',
            model: '',
            yearOfFabrication: 0,
            colore: '',
            km: 0,
            typCarburant: '',
            dateAchat: '',
            idOwner: null,
            etatCars: ''
          });
        } catch (error) {
          console.error('Error adding vehicle', error);
          alert('Failed to add vehicle');
        }
    };
  
    return (
        <div className="p-4">
          <h2 className="text-2xl mb-4">Vehicle Management</h2>
          <form onSubmit={handleVehicleSubmit} className="space-y-4">
            <input
              type="text"
              placeholder="Registration Number"
              value={vehicle.regestrationNumber}
              onChange={(e) => setVehicle({...vehicle, regestrationNumber: e.target.value})}
              className="w-full p-2 border rounded"
              required
            />
            <input
              type="text"
              placeholder="Brand (Marque)"
              value={vehicle.marque}
              onChange={(e) => setVehicle({...vehicle, marque: e.target.value})}
              className="w-full p-2 border rounded"
              required
            />
            <input
              type="text"
              placeholder="Model"
              value={vehicle.model}
              onChange={(e) => setVehicle({...vehicle, model: e.target.value})}
              className="w-full p-2 border rounded"
              required
            />
            <input
              type="number"
              placeholder="Year of Fabrication"
              value={vehicle.yearOfFabrication}
              onChange={(e) => setVehicle({...vehicle, yearOfFabrication: Number(e.target.value)})}
              className="w-full p-2 border rounded"
              required
            />
            <input
              type="text"
              placeholder="Color"
              value={vehicle.colore}
              onChange={(e) => setVehicle({...vehicle, colore: e.target.value})}
              className="w-full p-2 border rounded"
            />
            <input
              type="number"
              placeholder="Kilometers"
              value={vehicle.km}
              onChange={(e) => setVehicle({...vehicle, km: Number(e.target.value)})}
              className="w-full p-2 border rounded"
            />
            <select
              value={vehicle.typCarburant}
              onChange={(e) => setVehicle({...vehicle, typCarburant: e.target.value})}
              className="w-full p-2 border rounded"
              required
            >
              <option value="">Select Fuel Type</option>
              <option value="ESSENCE">Gasoline</option>
              <option value="DIESEL">Diesel</option>
              <option value="ELECTRIC">Electric</option>
            </select>
            <input
              type="date"
              placeholder="Purchase Date"
              value={vehicle.dateAchat}
              onChange={(e) => setVehicle({...vehicle, dateAchat: e.target.value})}
              className="w-full p-2 border rounded"
            />
            <input
              type="number"
              placeholder="Owner ID"
              value={vehicle.idOwner || ''}
              onChange={(e) => setVehicle({...vehicle, idOwner: Number(e.target.value)})}
              className="w-full p-2 border rounded"
            />
            <select
              value={vehicle.etatCars}
              onChange={(e) => setVehicle({...vehicle, etatCars: e.target.value})}
              className="w-full p-2 border rounded"
            >
              <option value="">Select Vehicle Status</option>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
              <option value="MAINTENANCE">Under Maintenance</option>
            </select>
            <button 
              type="submit" 
              className="bg-green-500 text-white p-2 rounded w-full"
            >
              Add Vehicle
            </button>
          </form>
        </div>
    );
};

export default VehicleManagement;