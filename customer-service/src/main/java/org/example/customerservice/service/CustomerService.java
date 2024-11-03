package org.example.customerservice.service;

import lombok.RequiredArgsConstructor;
import org.example.customerservice.dto.CustomerDTO;
import org.example.customerservice.entities.Customer;
import org.example.customerservice.event.CustomerEvent;
import org.example.customerservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    //private final KafkaTemplate<String, CustomerEvent> kafkaTemplate;


    public CustomerDTO createCustomer(CustomerDTO customerDTO){
        Customer customer = mapToEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);

        CustomerEvent event = new CustomerEvent();
        event.setEventType("CUSTOMER_CREATED");
        event.setCustomerId(savedCustomer.getId());
        event.setCustomerEmail(savedCustomer.getEmail());

        //kafkaTemplate.send("customer-events", event);
        return mapToDTO(savedCustomer);
    }




    private Customer mapToEntity(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setIdentityNumber(dto.getIdentityNumber());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setAddress(dto.getAddress());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        return customer;
    }
    private CustomerDTO mapToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setIdentityNumber(customer.getIdentityNumber());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setAddress(customer.getAddress());
        dto.setPhone(customer.getPhone());
        dto.setEmail(customer.getEmail());
        return dto;
    }
}
