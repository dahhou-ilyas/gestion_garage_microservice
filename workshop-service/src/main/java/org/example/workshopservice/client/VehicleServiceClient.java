package org.example.workshopservice.client;

import org.example.workshopservice.dto.CarsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "CARS-SERVICE")
public interface VehicleServiceClient {

    @GetMapping("/api/cars/{id}")
    CarsDTO getCarById(@PathVariable Long id);

    @PutMapping("/api/cars/{id}/{status}")
    void updateVehicleStatus(@PathVariable Long id, @PathVariable String status);
}
