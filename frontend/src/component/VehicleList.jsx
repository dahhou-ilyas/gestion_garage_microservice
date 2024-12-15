import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BACKEND_URL } from '../../config';

const VehicleList = () => {
  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchVehicles = async () => {
      try {
        const response = await axios.get(`${BACKEND_URL}/cars`);
        setVehicles(response.data);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch vehicles');
        setLoading(false);
      }
    };

    fetchVehicles();
  }, []);

  if (loading) return <div>Loading vehicles...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div className="p-4">
      <h2 className="text-2xl mb-4">Registered Vehicles</h2>
      <table className="w-full border-collapse border">
        <thead>
          <tr className="bg-gray-200">
            <th className="border p-2">Registration Number</th>
            <th className="border p-2">Brand</th>
            <th className="border p-2">Model</th>
            <th className="border p-2">Year</th>
            <th className="border p-2">Fuel Type</th>
            <th className="border p-2">Status</th>
          </tr>
        </thead>
        <tbody>
          {vehicles.map((vehicle) => (
            <tr key={vehicle.id} className="hover:bg-gray-100">
              <td className="border p-2">{vehicle.regestrationNumber}</td>
              <td className="border p-2">{vehicle.marque}</td>
              <td className="border p-2">{vehicle.model}</td>
              <td className="border p-2">{vehicle.yearOfFabrication}</td>
              <td className="border p-2">{vehicle.typCarburant}</td>
              <td className="border p-2">{vehicle.etatCars}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default VehicleList;