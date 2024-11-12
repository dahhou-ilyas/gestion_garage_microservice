package org.example.billingservice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MaintenanceWorkDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private MaintenanceStatus status;
    private BigDecimal estimatedCost;
    private CarsDTO vehicle;
    private CustomerDTO customer;
}

