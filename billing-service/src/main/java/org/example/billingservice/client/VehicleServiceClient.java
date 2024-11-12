package org.example.billingservice.client;

import org.example.billingservice.dto.CarsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cars-service")
public interface VehicleServiceClient {
    @GetMapping("/api/cars/{id}")
    CarsDTO getCarById(@PathVariable Long id);
}