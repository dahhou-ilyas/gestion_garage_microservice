package org.example.billingservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDTO {
    private Long customerId;
    private String type; // INVOICE, REMINDER, etc.
    private String message;
    private String details; // PDF URL, additional info
    private LocalDateTime createdAt;
}