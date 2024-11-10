package org.example.workshopservice.dto;

import org.example.workshopservice.entities.MaintenanceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MaintenanceWorkDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private MaintenanceStatus status;
    private String vehicleId;
    private String customerId;
    private BigDecimal estimatedCost;
    private CarsDTO vehicle;
    private CustomerDTO customer;
}
