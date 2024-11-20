package org.example.billingservice.listenner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.billingservice.dto.NotificationDTO;
import org.example.billingservice.entities.Invoice;
import org.example.billingservice.repository.InvoiceRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceListener {
    private InvoiceRepository invoiceRepository;
    
    private ObjectMapper objectMapper;
    
    private KafkaTemplate<String,String> kafkaTemplate;


    @KafkaListener(topics = "invoice-generated", groupId = "invoice-group")
    public void sendInvoiceNotification(String invoiceId) throws JsonProcessingException {
        Invoice invoice = invoiceRepository.findById(Long.parseLong(invoiceId))
                .orElseThrow();

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .customerId(invoice.getCustomerId())
                .type("INVOICE")
                .message("Nouvelle facture générée: " + invoice.getInvoiceNumber())
                .details(invoice.getPdfUrl())
                .build();

        String notification = objectMapper.writeValueAsString(notificationDTO);

        kafkaTemplate.send("notifications", notification);
    }
}
