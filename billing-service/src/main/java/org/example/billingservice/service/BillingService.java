package org.example.billingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.billingservice.client.CustomerServiceClient;
import org.example.billingservice.client.VehicleServiceClient;
import org.example.billingservice.dto.CarsDTO;
import org.example.billingservice.dto.CustomerDTO;
import org.example.billingservice.dto.MaintenanceWorkDTO;
import org.example.billingservice.entities.Invoice;
import org.example.billingservice.entities.InvoiceStatus;
import org.example.billingservice.repository.InvoiceRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillingService {
    private final InvoiceRepository invoiceRepository;
    private final CustomerServiceClient customerServiceClient;
    private final VehicleServiceClient vehicleServiceClient;
    private final PDFGeneratorService pdfGeneratorService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final BigDecimal TAX_RATE = BigDecimal.valueOf(0.2); // 20% tax rate
    private final int PAYMENT_DUE_DAYS = 30;

    @KafkaListener(topics = "maintenance-completed")
    @Transactional
    public void handleMaintenanceCompleted(MaintenanceWorkDTO maintenanceWork) {
        log.info("Received maintenance completed event for work ID: {}", maintenanceWork.getId());

        try {
            if (invoiceRepository.findByMaintenanceWorkId(maintenanceWork.getId()).isPresent()) {
                log.warn("Invoice already exists for maintenance work ID: {}", maintenanceWork.getId());
                return;
            }
            Invoice invoice = createInvoice(maintenanceWork);
            generateAndSendInvoice(invoice);

            kafkaTemplate.send("invoice-generated", String.valueOf(invoice.getId()));

            log.info("Invoice successfully generated and sent for maintenance work ID: {}", maintenanceWork.getId());

        } catch (Exception e) {
            log.error("Error processing maintenance completed event", e);
            throw new RuntimeException("Failed to process maintenance completion", e);
        }
    }
    @Transactional
    protected Invoice createInvoice(MaintenanceWorkDTO maintenanceWork){
        CustomerDTO customer = customerServiceClient.getCustomerById(maintenanceWork.getCustomer().getId());
        CarsDTO vehicle = vehicleServiceClient.getCarById(maintenanceWork.getVehicle().getId());

        BigDecimal subTotal = maintenanceWork.getEstimatedCost();
        BigDecimal taxAmount = subTotal.multiply(TAX_RATE);
        BigDecimal total = subTotal.add(taxAmount);

        Invoice invoice = Invoice.builder()
                .InvoiceNumber(generateInvoiceNumber())
                .CustomerId(customer.getId())
                .carId(vehicle.getId())
                .maintenanceWorkId(maintenanceWork.getId())
                .issueDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusDays(PAYMENT_DUE_DAYS))
                .status(InvoiceStatus.ISSUED)
                .subTotal(subTotal)
                .taxRate(TAX_RATE)
                .taxAmount(taxAmount)
                .total(total)
                .description(generateInvoiceDescription(maintenanceWork, vehicle))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return invoiceRepository.save(invoice);

    }

    @Transactional
    protected void generateAndSendInvoice(Invoice invoice){
        try{
            CustomerDTO customer = customerServiceClient.getCustomerById(invoice.getCustomerId());
            CarsDTO vehicle = vehicleServiceClient.getCarById(invoice.getCarId());

            String pdfPath = pdfGeneratorService.generateInvoicePDF(invoice, customer, vehicle);
            invoice.setPdfUrl(pdfPath);
            invoiceRepository.save(invoice);

            String message = String.format(
                    "Invoice %s generated for customer %s %s, total amount: %s",
                    invoice.getInvoiceNumber(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    invoice.getTotal()
            );
            kafkaTemplate.send("notifications", message);
        } catch (Exception e) {
            log.error("Error generating and sending invoice", e);
            throw new RuntimeException("Failed to generate and send invoice", e);
        }
    }

    public List<Invoice> getInvoicesByCustomerId(Long customerId) {
        return invoiceRepository.findByCustomerId(customerId);
    }

    public List<Invoice> getInvoicesByStatus(InvoiceStatus status) {
        return invoiceRepository.findByStatus(status);
    }

    @Transactional
    public Invoice updateInvoiceStatus(Long invoiceId, InvoiceStatus newStatus) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setStatus(newStatus);
        invoice.setUpdatedAt(LocalDateTime.now());

        return invoiceRepository.save(invoice);
    }

    private String generateInvoiceNumber() {
        return "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateInvoiceDescription(MaintenanceWorkDTO maintenanceWork, CarsDTO vehicle) {
        return String.format(
                "Maintenance work performed on %s %s (%s)\nDescription: %s\nStart Time: %s\nEnd Time: %s",
                vehicle.getMarque(),
                vehicle.getModel(),
                vehicle.getRegestrationNumber(),
                maintenanceWork.getDescription(),
                maintenanceWork.getStartTime(),
                maintenanceWork.getEndTime()
        );
    }
}
