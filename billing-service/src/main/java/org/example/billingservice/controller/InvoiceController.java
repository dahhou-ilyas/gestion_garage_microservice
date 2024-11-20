package org.example.billingservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.billingservice.entities.Invoice;
import org.example.billingservice.entities.InvoiceStatus;
import org.example.billingservice.service.BillingService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final BillingService billingService;

    @GetMapping("/customer/{customerId}")
    public List<Invoice> getCustomerInvoices(@PathVariable Long customerId) {
        return billingService.getInvoicesByCustomerId(customerId);
    }

    @PostMapping("/{invoiceId}/status")
    public Invoice updateInvoiceStatus(@PathVariable Long invoiceId, @RequestParam InvoiceStatus invoiceStatus){
        return billingService.updateInvoiceStatus(invoiceId,invoiceStatus);
    }

    @GetMapping("/pdf/{invoiceId}")
    public ResponseEntity<Resource> downloadInvoicePDF(@PathVariable Long invoiceId) throws MalformedURLException {
        Invoice invoice = billingService.getInvoiceById(invoiceId);
        Path pdfPath = Paths.get(invoice.getPdfUrl());
        System.out.println(pdfPath.toUri().toString());
        Resource resource = new UrlResource(pdfPath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + pdfPath.getFileName() + "\"")
                .body(resource);

    }
}
