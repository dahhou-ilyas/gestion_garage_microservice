package org.example.carsservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "cars")
public class Cars {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String regestrationNumber;
    @Column(nullable = false)
    private String marque;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private int yearOfFabrication;
    private String colore;
    @Column(nullable = false)
    private double km;
    private String typCarburant;
    @Column(nullable = false)
    private Date dateAchat;
    @Column(nullable = false)
    private Long idOwner;
    @Column(nullable = false)
    private String etatCars;
}
