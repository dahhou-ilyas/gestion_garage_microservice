package org.example.workshopservice.client;

import org.example.workshopservice.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerServiceClient {
    @GetMapping("/api/customers/{id}")
    CustomerDTO getCustomerById(@PathVariable Long id);
}
