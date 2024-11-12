package org.example.billingservice.repository;

import org.example.billingservice.entities.Invoice;
import org.example.billingservice.entities.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    List<Invoice> findByCustomerId(Long customerId);

    List<Invoice> findByStatus(InvoiceStatus status);

    Optional<Invoice> findByMaintenanceWorkId(Long maintenanceWorkId);

}
