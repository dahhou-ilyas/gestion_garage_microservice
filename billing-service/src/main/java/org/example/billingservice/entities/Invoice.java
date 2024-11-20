package org.example.billingservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String InvoiceNumber;
    private Long customerId;
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
