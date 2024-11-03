package org.example.customerservice.repository;

import org.example.customerservice.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByIdentityNumber(String indetityNumber);
    Optional<Customer> findCustomerByEmail(String email);
}
