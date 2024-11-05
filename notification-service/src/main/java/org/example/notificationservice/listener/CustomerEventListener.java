package org.example.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.event.CustomerEvent;
import org.example.notificationservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventListener {
    private final EmailService emailService;

    @KafkaListener(topics = "customer-events", groupId = "notification-service-group")
    public void handleCustomerEvent(CustomerEvent customerEvent){
        log.info("Received customer event: {}", customerEvent);

        if ("CUSTOMER_CREATED".equals(customerEvent.getEventType())) {
            log.info("Processing new customer registration: {}", customerEvent.getCustomerEmail());
            emailService.sendWelcomeEmail(customerEvent.getCustomerEmail(), customerEvent.getCustomerName());
        }

    }
}
