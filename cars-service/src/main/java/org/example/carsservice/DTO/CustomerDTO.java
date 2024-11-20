package org.example.carsservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private Long id;
    private String identityNumber;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
}