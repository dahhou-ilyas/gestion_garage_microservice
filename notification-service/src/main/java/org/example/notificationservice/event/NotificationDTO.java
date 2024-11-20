package org.example.notificationservice.event;

import lombok.Builder;
import lombok.Data;

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