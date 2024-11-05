package org.example.customerservice.event;

import lombok.Data;

@Data
public class CustomerEvent {
    private String eventType;
    private Long customerId;
    private String customerEmail;
    private String customerName;
}
