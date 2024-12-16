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
import org.example.workshopservice.entities.MaintenanceStatus;
import org.example.workshopservice.entities.MaintenanceWork;
import org.example.workshopservice.event.MaintenanceCompletedEvent;
import org.example.workshopservice.event.MaintenanceScheduledEvent;
import org.example.workshopservice.repository.MaintenanceWorkRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public MaintenanceWorkDTO completeMaintenance(Long id, BigDecimal finalCost) throws JsonProcessingException {
        MaintenanceWork work = maintenanceWorkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance work not found"));

        work.setStatus(MaintenanceStatus.COMPLETED);
        work = maintenanceWorkRepository.save(work);

        vehicleServiceClient.updateVehicleStatus(work.getVehicleId(),"FUNCTIONAL");

        MaintenanceCompletedEvent event = MaintenanceCompletedEvent.builder()
                .maintenanceId(work.getId())
                .vehicleId(work.getVehicleId())
                .customerId(work.getCustomerId())
                .completionTime(LocalDateTime.now())
                .finalCost(finalCost)
                .description(work.getDescription())
                .build();

        String eventJson = objectMapper.writeValueAsString(event);

        kafkaTemplate.send("maintenance-completed", eventJson);

        CarsDTO carDTO = vehicleServiceClient.getCarById(work.getVehicleId());

        CustomerDTO customerDTO = customerServiceClient.getCustomerById(work.getCustomerId());

        return mapToDTO(work, carDTO, customerDTO);
    }

    public List<MaintenanceWorkDTO> getScheduleForDay(LocalDate date){
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return maintenanceWorkRepository.findByStartTimeBetween(start, end)
                .stream()
                .map(work -> {
                    CarsDTO vehicle = vehicleServiceClient.getCarById(work.getVehicleId());
                    CustomerDTO customer = customerServiceClient.getCustomerById(work.getCustomerId());
                    return mapToDTO(work, vehicle, customer);
                })
                .collect(Collectors.toList());
    }

    private boolean isTimeSlotOccupied(LocalDateTime start, LocalDateTime end) {
        List<MaintenanceWork> existingWorks = maintenanceWorkRepository
                .findByStartTimeBetween(start, end);
        return !existingWorks.isEmpty();
    }

    private MaintenanceWorkDTO mapToDTO(MaintenanceWork work, CarsDTO vehicle, CustomerDTO customer) {
        return MaintenanceWorkDTO.builder()
                .id(work.getId())
                .startTime(work.getStartTime())
                .endTime(work.getEndTime())
                .description(work.getDescription())
                .status(work.getStatus())
                .vehicleId(work.getVehicleId())
                .customerId(work.getCustomerId())
                .estimatedCost(work.getEstimatedCost())
                .vehicle(vehicle)
                .customer(customer)
                .build();
    }


    public List<MaintenanceWorkDTO> getAllMaintenanceWorks() {
        return maintenanceWorkRepository.findAllByOrderByStartTimeDesc()
                .stream()
                .map(work -> {
                    CarsDTO vehicle = vehicleServiceClient.getCarById(work.getVehicleId());
                    CustomerDTO customer = customerServiceClient.getCustomerById(work.getCustomerId());
                    return mapToDTO(work, vehicle, customer);
                })
                .collect(Collectors.toList());
    }

}
