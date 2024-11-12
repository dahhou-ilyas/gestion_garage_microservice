package org.example.billingservice.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import org.example.billingservice.dto.CarsDTO;
import org.example.billingservice.dto.CustomerDTO;
import org.example.billingservice.entities.Invoice;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class PDFGeneratorService {
    public String generateInvoicePDF(Invoice invoice, CustomerDTO customerDTO, CarsDTO carsDTO){
        try {
            String fileName = "invoice_" + invoice.getInvoiceNumber() + ".pdf";
            String filePath = "invoices/" + fileName;

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("GARAGE AUTO SERVICE")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Invoice #: " + invoice.getInvoiceNumber()));
            document.add(new Paragraph("Date: " + invoice.getIssueDate().format(DateTimeFormatter.ISO_DATE)));

            // Add customer details
            document.add(new Paragraph("\nBill To:"));
            document.add(new Paragraph(customerDTO.getFirstName()));
            document.add(new Paragraph(customerDTO.getAddress()));

            // Add vehicle details
            document.add(new Paragraph("\nVehicle Details:"));
            document.add(new Paragraph(carsDTO.getMarque() + " " + carsDTO.getModel()));
            document.add(new Paragraph("VIN: " + carsDTO.getRegestrationNumber()));

            // Add service details
            document.add(new Paragraph("\nService Description:"));
            document.add(new Paragraph(invoice.getDescription()));

            // Add amounts
            document.add(new Paragraph("\nSubtotal: $" + invoice.getSubTotal()));
            document.add(new Paragraph("Tax (" + invoice.getTaxRate() + "%): $" + invoice.getTaxAmount()));
            document.add(new Paragraph("Total: $" + invoice.getTotal())
                    .setBold());

            document.close();

            return filePath;
        }catch (IOException e){
            log.error("Error generating PDF", e);
            throw new RuntimeException("Failed to generate invoice PDF");
        }
    }
}
