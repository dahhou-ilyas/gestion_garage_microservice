package org.example.notificationservice.event;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomerEvent {
    private String eventType;
    private Long customerId;
    private String customerEmail;
    private String customerName;
}
