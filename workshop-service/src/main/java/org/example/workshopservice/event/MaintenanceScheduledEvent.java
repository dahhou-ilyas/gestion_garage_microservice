package org.example.workshopservice.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MaintenanceScheduledEvent {
    private Long maintenanceId;
    private Long vehicleId;
    private Long customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private BigDecimal estimatedCost;
}
