package org.example.notificationservice.event;

import lombok.Data;

@Data
public class CustomerEvent {
    private String eventType;
    private Long customerId;
    private String customerEmail;
    private String customerName;
}
