package org.example.carsservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarCreatedEvent {
    private Long carId;
    private Long ownerId;
    private String registrationNumber;
    private String marque;
    private String model;
}
