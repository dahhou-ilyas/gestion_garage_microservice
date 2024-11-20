package org.example.billingservice.listenner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.billingservice.client.CustomerServiceClient;
import org.example.billingservice.dto.CustomerDTO;
import org.example.billingservice.dto.NotificationDTO;
import org.example.billingservice.entities.Invoice;
import org.example.billingservice.repository.InvoiceRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvoiceListener {
    private final InvoiceRepository invoiceRepository;
    
    private final ObjectMapper objectMapper;
    
    private final KafkaTemplate<String,String> kafkaTemplate;

    private final CustomerServiceClient customerServiceClient;


    @KafkaListener(topics = "invoice-generated", groupId = "invoice-group")
    public void sendInvoiceNotification(String invoiceId) throws IOException {
        Invoice invoice = invoiceRepository.findById(Long.parseLong(invoiceId))
                .orElseThrow();

        CustomerDTO customer = customerServiceClient.getCustomerById(invoice.getCustomerId());


        Path pdfPath = Paths.get(invoice.getPdfUrl());

        byte[] pdfContent = Files.readAllBytes(pdfPath);

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .customerId(invoice.getCustomerId())
                .type("INVOICE")
                .message("Nouvelle facture générée: " + invoice.getInvoiceNumber())
                .pdfContent(Base64.getEncoder().encodeToString(pdfContent)) // Encoder en Base64
                .invoiceNumber(invoice.getInvoiceNumber())
                .mail(customer.getEmail())
                .build();

        String notification = objectMapper.writeValueAsString(notificationDTO);
        kafkaTemplate.send("invoice-topic", notification);
    }
}
