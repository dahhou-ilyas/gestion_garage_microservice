package org.example.notificationservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.event.CarCreatedEvent;
import org.example.notificationservice.service.EmailService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CarCreatedEventListenner {
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @KafkaListener(topics = "customer-events", groupId = "message-group")
    public void handleCarsCreationToGarage(String event) {
        try{
            CarCreatedEvent carCreatedEvent = objectMapper.readValue(event,CarCreatedEvent.class);
            log.info("Received Car event: {}", event);

            String carDetails = formatCarDetails(carCreatedEvent);
            String redisKey = "car_notification:" + carCreatedEvent.getCarId();
            Boolean wasProcessed = redisTemplate.hasKey(redisKey);

            if (Boolean.FALSE.equals(wasProcessed)) {
                // Send email
                emailService.sendCarRegistrationEmail(
                        carCreatedEvent.getMailOwner(),
                        formatCustomerName(carCreatedEvent),
                        carDetails
                );

                // Store in Redis to prevent duplicate processing
                redisTemplate.opsForValue().set(redisKey, "processed", 24, TimeUnit.HOURS);
                log.info("Car creation notification sent for car ID: {}", carCreatedEvent.getCarId());
            } else {
                log.info("Notification already sent for car ID: {}", carCreatedEvent.getCarId());
            }

        } catch (JsonProcessingException e) {
            log.error("Error deserializing car created event: {}", e.getMessage());
            throw new RuntimeException("Failed to process car created event", e);
        } catch (Exception e) {
            log.error("Error processing car created event: {}", e.getMessage());
            throw new RuntimeException("Failed to process car created event", e);
        }
    }
    private String formatCustomerName(CarCreatedEvent event) {
        return event.getFirstname() + " " + event.getLastname();
    }

    private String formatCarDetails(CarCreatedEvent event) {
        return String.format("""
            <table style="width: 100%%; border-collapse: collapse;">
                <tr>
                    <td style="padding: 8px; color: #666;">Marque :</td>
                    <td style="padding: 8px;"><strong>%s</strong></td>
                </tr>
                <tr>
                    <td style="padding: 8px; color: #666;">Modèle :</td>
                    <td style="padding: 8px;"><strong>%s</strong></td>
                </tr>
                <tr>
                    <td style="padding: 8px; color: #666;">Immatriculation :</td>
                    <td style="padding: 8px;"><strong>%s</strong></td>
                </tr>
            </table>
            """,
                event.getMarque(),
                event.getModel(),
                event.getRegistrationNumber()
        );
    }
}
