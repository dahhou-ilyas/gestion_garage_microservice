package org.example.workshopservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private Long id;
    private String identityNumber;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String email;
}
