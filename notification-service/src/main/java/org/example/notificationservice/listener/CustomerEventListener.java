package org.example.notificationservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.event.CustomerEvent;
import org.example.notificationservice.service.EmailService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventListener {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @KafkaListener(topics = "customer-events", groupId = "message-group")
    public void handleCustomerEvent(String event) {
        try {
            CustomerEvent customerEvent = objectMapper.readValue(event, CustomerEvent.class);

            log.info("Received customer event: {}", event);

            if ("CUSTOMER_CREATED".equals(customerEvent.getEventType())) {
                String userEmail = customerEvent.getCustomerEmail();

                if (redisTemplate.hasKey(userEmail)) {
                    log.info("Event for user {} has already been processed, skipping email.", userEmail);
                    return;
                }
                log.info("Processing new customer registration: {}", customerEvent.getCustomerEmail());
                emailService.sendWelcomeEmail(customerEvent.getCustomerEmail(), customerEvent.getCustomerName());

                redisTemplate.opsForValue().set(userEmail, "value");
                log.info("Email sent to {}", userEmail);
            }
        } catch (Exception e) {
            log.error("Error processing customer event: {}", event, e);
        }
    }
}
