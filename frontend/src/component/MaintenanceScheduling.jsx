import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BACKEND_URL } from '../../config';

const MaintenanceScheduling = () => {
    const [customers, setCustomers] = useState([]);
    const [vehicles, setVehicles] = useState([]);
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

    // État pour stocker le client sélectionné
    const [selectedCustomer, setSelectedCustomer] = useState(null);

    // Charger les clients au montage du composant
    useEffect(() => {
        const fetchCustomers = async () => {
            try {
                const response = await axios.get(`${BACKEND_URL}/customer-service/api/customers`);
                setCustomers(response.data);
            } catch (error) {
                console.error('Erreur lors du chargement des clients', error);
            }
        };

        fetchCustomers();
    }, []);

    // Charger les véhicules du client sélectionné
    useEffect(() => {
        const fetchVehiclesForCustomer = async () => {
            if (selectedCustomer) {
                try {
                    const response = await axios.get(`${BACKEND_URL}/cars-service/api/cars/customer/${selectedCustomer}`);
                    setVehicles(response.data);
                } catch (error) {
                    console.error('Erreur lors du chargement des véhicules', error);
                    setVehicles([]);
                }
            } else {
                setVehicles([]);
            }
        };

        fetchVehiclesForCustomer();
    }, [selectedCustomer]);

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
            // Réinitialiser le formulaire
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
            setSelectedCustomer(null);
        } catch (error) {
            console.error('Error scheduling maintenance', error);
            alert('Failed to schedule maintenance');
        }
    };

    return (
        <div className="p-4">
            <h2 className="text-2xl mb-4">Maintenance Scheduling</h2>
            <form onSubmit={handleMaintenanceSchedule} className="space-y-4">
                <div>
                    <label className="block mb-2">Select Customer</label>
                    <select
                        value={selectedCustomer || ''}
                        onChange={(e) => {
                            const customerId = Number(e.target.value);
                            setSelectedCustomer(customerId);
                            setMaintenance(prev => ({
                                ...prev,
                                customerId: customerId,
                                vehicleId: null // Réinitialiser le véhicule
                            }));
                        }}
                        className="w-full p-3 border border-gray-300 rounded-lg bg-white text-gray-700 focus:ring-2 focus:ring-blue-500 focus:outline-none hover:border-blue-400 transition duration-300"
                        required
                    >
                        <option value="">Select a Customer</option>
                        {customers.map(customer => (
                            <option 
                                key={customer.id} 
                                value={customer.id}
                            >
                                {customer.firstName} {customer.lastName} (ID: {customer.id})
                            </option>
                        ))}
                    </select>
                </div>

                {selectedCustomer && (
                    <div>
                        <label className="block mb-2">Select Vehicle</label>
                        <select
                            value={maintenance.vehicleId || ''}
                            onChange={(e) => setMaintenance(prev => ({
                                ...prev,
                                vehicleId: Number(e.target.value)
                            }))}
                            className="w-full p-2 border rounded"
                            required
                            disabled={vehicles.length === 0}
                        >
                            <option value="">Select a Vehicle</option>
                            {vehicles.map(vehicle => (
                                <option 
                                    key={vehicle.id} 
                                    value={vehicle.id}
                                >
                                    {vehicle.brand} {vehicle.model} (Plate: {vehicle.licensePlate})
                                </option>
                            ))}
                        </select>
                    </div>
                )}

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
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="COMPLETED">Completed</option>
                </select>

                <button 
                    type="submit" 
                    className="bg-purple-500 text-white p-2 rounded w-full"
                    disabled={!maintenance.customerId || !maintenance.vehicleId}
                >
                    Schedule Maintenance
                </button>
            </form>
        </div>
    );
};

export default MaintenanceScheduling;