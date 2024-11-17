package org.example.carsservice.client;


import org.example.carsservice.DTO.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${customer.service.url}")
public interface CustomerClient {
    @GetMapping("/api/customers/{id}")
    CustomerDTO customerExists(@PathVariable("id") Long id);


}