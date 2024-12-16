package org.example.billingservice.mapper;

import org.example.billingservice.dto.InvoiceDTO;
import org.example.billingservice.entities.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    public InvoiceDTO toDTO(Invoice invoice) {
        if (invoice == null) {
            return null;
        }

        return InvoiceDTO.builder()
                .invoiceNumber(invoice.getInvoiceNumber())
                .customerId(invoice.getCustomerId())
                .carId(invoice.getCarId())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .status(invoice.getStatus())
                .total(invoice.getTotal())
                .build();
    }

    public Invoice toEntity(InvoiceDTO invoiceDTO) {
        if (invoiceDTO == null) {
            return null;
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setCustomerId(invoiceDTO.getCustomerId());
        invoice.setCarId(invoiceDTO.getCarId());
        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setDueDate(invoiceDTO.getDueDate());
        invoice.setStatus(invoiceDTO.getStatus());
        invoice.setTotal(invoiceDTO.getTotal());

        return invoice;
    }
}