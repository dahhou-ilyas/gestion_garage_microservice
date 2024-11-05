package org.example.customerservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.customerservice.dto.CustomerDTO;
import org.example.customerservice.entities.Customer;
import org.example.customerservice.event.CustomerEvent;
import org.example.customerservice.repository.CustomerRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;
    public CustomerDTO createCustomer(CustomerDTO customerDTO) throws JsonProcessingException {
        Customer customer = mapToEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);

        CustomerEvent event = new CustomerEvent();
        event.setEventType("CUSTOMER_CREATED");
        event.setCustomerId(savedCustomer.getId());
        event.setCustomerEmail(savedCustomer.getEmail());
        event.setCustomerName(savedCustomer.getFirstName() + " " +savedCustomer.getLastName());

        String messageJson = objectMapper.writeValueAsString(event);

        kafkaTemplate.send("customer-events", messageJson);
        return mapToDTO(savedCustomer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) throws JsonProcessingException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        updateCustomerFields(customer, customerDTO);
        Customer updatedCustomer = customerRepository.save(customer);

        CustomerEvent event = new CustomerEvent();
        event.setEventType("CUSTOMER_UPDATED");
        event.setCustomerId(updatedCustomer.getId());
        event.setCustomerEmail(updatedCustomer.getEmail());
        event.setCustomerName(updatedCustomer.getFirstName() + " " +updatedCustomer.getLastName());

        String messageJson = objectMapper.writeValueAsString(event);

        kafkaTemplate.send("customer-events", messageJson);

        return mapToDTO(updatedCustomer);


    }

    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
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

    private void updateCustomerFields(Customer customer, CustomerDTO dto) {
        if(dto.getFirstName()!=null){
            customer.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName()!=null){
            customer.setLastName(dto.getLastName());
        }
        if(dto.getAddress()!=null){
            customer.setAddress(dto.getAddress());
        }
        if(dto.getPhone()!=null){
            customer.setPhone(dto.getPhone());
        }
        if (dto.getEmail()!=null){
            customer.setEmail(dto.getEmail());
        }
    }
}
