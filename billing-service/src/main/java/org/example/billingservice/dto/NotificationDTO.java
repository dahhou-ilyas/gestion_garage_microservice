package org.example.billingservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDTO {
    private Long customerId;
    private String type;
    private String message;
    private String pdfContent; // Contenu PDF encodé en Base64
    private String invoiceNumber;
    private String mail;
}