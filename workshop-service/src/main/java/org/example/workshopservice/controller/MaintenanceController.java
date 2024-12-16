package org.example.workshopservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.example.workshopservice.dto.MaintenanceWorkDTO;
import org.example.workshopservice.service.MaintenanceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<MaintenanceWorkDTO> scheduleMaintenance(@RequestBody MaintenanceWorkDTO dto) throws JsonProcessingException {
        return ResponseEntity.ok(maintenanceService.scheduleMaintenance(dto));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<MaintenanceWorkDTO> completeMaintenance(
            @PathVariable Long id,
            @RequestParam BigDecimal finalCost) throws JsonProcessingException {
        return ResponseEntity.ok(maintenanceService.completeMaintenance(id, finalCost));
    }

    @GetMapping("/schedule/{date}")
    public ResponseEntity<List<MaintenanceWorkDTO>> getScheduleForDay(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(maintenanceService.getScheduleForDay(date));
    }


    @GetMapping
    public ResponseEntity<List<MaintenanceWorkDTO>> getAllMaintenanceWorks() {
        return ResponseEntity.ok(maintenanceService.getAllMaintenanceWorks());
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countNumberOfWork(){
        return ResponseEntity.ok(maintenanceService.getAllMaintenanceWorks().size());
    }
}
