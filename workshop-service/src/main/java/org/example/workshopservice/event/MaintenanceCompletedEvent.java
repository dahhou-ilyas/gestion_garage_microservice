package org.example.workshopservice.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MaintenanceCompletedEvent {
    private Long maintenanceId;
    private Long vehicleId;
    private Long customerId;
    private LocalDateTime completionTime;
    private BigDecimal finalCost;
    private String description;
}