package org.example.workshopservice.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MaintenanceCompletedEvent {
    private Long maintenanceId;
    private String vehicleId;
    private String customerId;
    private LocalDateTime completionTime;
    private BigDecimal finalCost;
}