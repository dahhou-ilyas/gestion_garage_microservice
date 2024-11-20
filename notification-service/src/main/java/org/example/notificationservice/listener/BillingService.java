package org.example.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.event.NotificationDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final ObjectMapper objectMapper;
    private final JavaMailSenderImpl mailSender;

    @KafkaListener(topics = "invoice-topic", groupId = "invoice-group")
    public void processeInvoiveNotification(String notificationJson) throws IOException, MessagingException {
        NotificationDTO notificationDTO = objectMapper.readValue(notificationJson,NotificationDTO.class);

        byte[] pdfBytes = Base64.getDecoder().decode(notificationDTO.getPdfContent());

        String fileName = "invoice_" + notificationDTO.getInvoiceNumber() + ".pdf";

        Path filePath = Paths.get("invoices", fileName);
        Files.write(filePath, pdfBytes);

        sendEmailWithAttachment(notificationDTO, filePath);
    }

    private void sendEmailWithAttachment(NotificationDTO notification, Path pdfPath) throws MessagingException {
        // Logique d'envoi d'email avec pièce jointe
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(notification.getMail());
        helper.setSubject("Nouvelle Facture");
        helper.setText(notification.getMessage());

        FileSystemResource file = new FileSystemResource(pdfPath.toFile());
        helper.addAttachment(file.getFilename(), file);

        mailSender.send(message);
    }
}
