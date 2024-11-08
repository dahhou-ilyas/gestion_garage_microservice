package org.example.carsservice.Controller;

import lombok.RequiredArgsConstructor;
import org.example.carsservice.DTO.CarsDTO;
import org.example.carsservice.Service.CarsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarsController {
    private final CarsService carsService;

    @PostMapping
    public ResponseEntity<CarsDTO> createCar(@RequestBody CarsDTO carsDTO) {
        return new ResponseEntity<>(carsService.addCar(carsDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarsDTO> updateCar(@PathVariable Long id, @RequestBody CarsDTO carsDTO) {
        return ResponseEntity.ok(carsService.updateCar(id, carsDTO));
    }

    @GetMapping
    public ResponseEntity<List<CarsDTO>> getAllCars() {
        return ResponseEntity.ok(carsService.getAllCars());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarsDTO> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carsService.getCarById(id));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<CarsDTO>> getCarsByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(carsService.getCarsByOwner(ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carsService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

}
