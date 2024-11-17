package org.example.carsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CarsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarsServiceApplication.class, args);
    }

}
