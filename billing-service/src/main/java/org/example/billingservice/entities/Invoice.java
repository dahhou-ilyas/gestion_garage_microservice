package org.example.billingservice.entities;

import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Invoice {
    private long id;

    private String InvoiceNumber;
    private Long CustomerId;
    private Long carId;
    private Long maintenanceWorkId;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;

    private InvoiceStatus status;

    private BigDecimal subTotal;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal total;

    @Column(length = 1000)
    private String description;

    private String pdfUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
