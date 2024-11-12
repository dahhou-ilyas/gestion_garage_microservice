package org.example.billingservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CarsDTO {
    private Long id;
    private String regestrationNumber;
    private String marque;
    private String model;
    private int yearOfFabrication;
    private String colore;
    private double km;
    private String typCarburant;
    private Date dateAchat;
    private Long idOwner;
    private String etatCars;
}