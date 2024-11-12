package org.example.billingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.billingservice.client.CustomerServiceClient;
import org.example.billingservice.client.VehicleServiceClient;
import org.example.billingservice.dto.MaintenanceWorkDTO;
import org.example.billingservice.repository.InvoiceRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillingService {
    private final InvoiceRepository invoiceRepository;
    private final CustomerServiceClient customerServiceClient;
    private final VehicleServiceClient vehicleServiceClient;
    private final PDFGeneratorService pdfGeneratorService;
    private final KafkaTemplate<String,String> kafkaTemplate;

    private final BigDecimal taxRate = BigDecimal.valueOf(0.2);

    @KafkaListener(topics = "maintenance-completed")
    public void handleMaintenanceCompleted(MaintenanceWorkDTO maintenanceWork) {
        log.info("Received maintenance completed event for work ID: {}", maintenanceWork.getId());

        try {
            createInvoice(maintenanceWork);
        } catch (Exception e) {
            log.error("Error processing maintenance completed event", e);
        }
    }
}
