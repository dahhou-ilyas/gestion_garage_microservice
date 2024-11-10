package org.example.workshopservice.repository;

import org.example.workshopservice.entities.MaintenanceStatus;
import org.example.workshopservice.entities.MaintenanceWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceWorkRepository extends JpaRepository<MaintenanceWork,Long> {
    List<MaintenanceWork> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<MaintenanceWork> findByVehicleId(Long vehicleId);
    List<MaintenanceWork> findByCustomerId(Long customerId);
    List<MaintenanceWork> findByStatus(MaintenanceStatus status);
}
