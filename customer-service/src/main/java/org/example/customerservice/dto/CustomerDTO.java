package org.example.customerservice.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String identityNumber;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
}
