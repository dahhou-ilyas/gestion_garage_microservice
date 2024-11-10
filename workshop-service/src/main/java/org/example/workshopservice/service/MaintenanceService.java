package org.example.workshopservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.workshopservice.client.CustomerServiceClient;
import org.example.workshopservice.client.VehicleServiceClient;
import org.example.workshopservice.dto.CarsDTO;
import org.example.workshopservice.dto.CustomerDTO;
import org.example.workshopservice.dto.MaintenanceWorkDTO;
import org.example.workshopservice.entities.MaintenanceWork;
import org.example.workshopservice.event.MaintenanceScheduledEvent;
import org.example.workshopservice.repository.MaintenanceWorkRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MaintenanceService {
    private final MaintenanceWorkRepository maintenanceWorkRepository;
    private final VehicleServiceClient vehicleServiceClient;
    private final CustomerServiceClient customerServiceClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public MaintenanceWorkDTO scheduleMaintenance(MaintenanceWorkDTO dto) throws JsonProcessingException {
        CarsDTO carsDTO = vehicleServiceClient.getCarById(dto.getVehicleId());

        if (carsDTO == null){
            throw new RuntimeException("car not found");
        }

        CustomerDTO customerDTO= customerServiceClient.getCustomerById(dto.getCustomerId());

        if (customerDTO == null) {
            throw new RuntimeException("Customer not found");
        }

        if (isTimeSlotOccupied(dto.getStartTime(), dto.getEndTime())) {
            throw new RuntimeException("Time slot is not available");
        }

        MaintenanceWork work = MaintenanceWork.builder()
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .vehicleId(dto.getVehicleId())
                .customerId(dto.getCustomerId())
                .estimatedCost(dto.getEstimatedCost())
                .build();

        work = maintenanceWorkRepository.save(work);

        vehicleServiceClient.updateVehicleStatus(dto.getVehicleId(), "IN_MAINTENANCE");

        MaintenanceScheduledEvent event = MaintenanceScheduledEvent.builder()
                .maintenanceId(work.getId())
                .vehicleId(work.getVehicleId())
                .customerId(work.getCustomerId())
                .startTime(work.getStartTime())
                .endTime(work.getEndTime())
                .description(work.getDescription())
                .estimatedCost(work.getEstimatedCost())
                .build();

        String eventJson = objectMapper.writeValueAsString(event);

        kafkaTemplate.send("maintenance-scheduled", eventJson);

        return mapToDTO(work, carsDTO, customerDTO);


    }

}
