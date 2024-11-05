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

    @KafkaListener(topics = "customer-events")
    public void handleCustomerEvent(CustomerEvent customerEvent) {
        System.out.println("?????????????????????????");
        System.out.println(customerEvent);
        System.out.println("?????????????????????????");
        try {
            System.out.println(customerEvent.getEventType());
            log.info("Received customer event: {}", customerEvent);

            if ("CUSTOMER_CREATED".equals(customerEvent.getEventType())) {
                log.info("Processing new customer registration: {}", customerEvent.getCustomerEmail());
                emailService.sendWelcomeEmail(customerEvent.getCustomerEmail(), customerEvent.getCustomerName());
            }
        } catch (Exception e) {
            log.error("Error processing customer event: {}", customerEvent, e);
        }
    }
}
