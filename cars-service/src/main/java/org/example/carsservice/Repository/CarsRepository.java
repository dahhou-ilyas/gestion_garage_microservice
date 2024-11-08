package org.example.carsservice.Repository;

import org.example.carsservice.entities.Cars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarsRepository extends JpaRepository<Cars, Long> {
    Optional<Cars> findByRegestrationNumber(String regestrationNumber);
    List<Cars> findByIdOwner(Long idOwner);
}