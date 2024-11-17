package org.example.carsservice.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.carsservice.DTO.CarsDTO;
import org.example.carsservice.DTO.CustomerDTO;
import org.example.carsservice.Exceptions.CarNotFoundException;
import org.example.carsservice.Exceptions.CustomerNotFoundException;
import org.example.carsservice.Exceptions.DuplicateCarException;
import org.example.carsservice.Repository.CarsRepository;
import org.example.carsservice.client.CustomerClient;
import org.example.carsservice.entities.Cars;
import org.example.carsservice.event.CarCreatedEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarsService {
    private final CarsRepository carsRepository;
    private final CustomerClient customerClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String CAR_CREATED_TOPIC = "car-created";

    public CarsDTO addCar(CarsDTO carsDTO) throws JsonProcessingException {
        CustomerDTO customerExists = customerClient.customerExists(carsDTO.getIdOwner());

        if (customerExists==null) {
            throw new CustomerNotFoundException("Le propriétaire avec l'ID " + carsDTO.getIdOwner() + " n'existe pas");
        }

        if (carsRepository.findByRegestrationNumber(carsDTO.getRegestrationNumber()).isPresent()) {
            throw new DuplicateCarException("Une voiture avec ce numéro d'immatriculation existe déjà");
        }
        Cars car = new Cars();
        BeanUtils.copyProperties(carsDTO, car);

        Cars savedCar = carsRepository.save(car);

        CarCreatedEvent event = new CarCreatedEvent(
                savedCar.getId(),
                savedCar.getIdOwner(),
                customerExists.getEmail(),
                customerExists.getFirstName(),
                customerExists.getLastName(),
                savedCar.getRegestrationNumber(),
                savedCar.getMarque(),
                savedCar.getModel()
        );
        String messageJson = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(CAR_CREATED_TOPIC, messageJson);

        CarsDTO savedCarDTO = new CarsDTO();
        BeanUtils.copyProperties(savedCar, savedCarDTO);
        return savedCarDTO;
    }

    public CarsDTO updateCar(Long id, CarsDTO carsDTO){
        Cars existingCar = carsRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Voiture non trouvée avec l'ID: " + id));


        if (!existingCar.getRegestrationNumber().equals(carsDTO.getRegestrationNumber()) &&
                carsRepository.findByRegestrationNumber(carsDTO.getRegestrationNumber()).isPresent()) {
            throw new DuplicateCarException("Ce numéro d'immatriculation est déjà utilisé");
        }

        BeanUtils.copyProperties(carsDTO, existingCar, "id");
        Cars updatedCar = carsRepository.save(existingCar);

        CarsDTO updatedCarDTO = new CarsDTO();
        BeanUtils.copyProperties(updatedCar, updatedCarDTO);
        return updatedCarDTO;

    }

    public List<CarsDTO> getAllCars() {
        return carsRepository.findAll().stream()
                .map(car -> {
                    CarsDTO dto = new CarsDTO();
                    BeanUtils.copyProperties(car, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public CarsDTO getCarById(Long id) {
        Cars car = carsRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Voiture non trouvée avec l'ID: " + id));

        CarsDTO carDTO = new CarsDTO();
        BeanUtils.copyProperties(car, carDTO);
        return carDTO;
    }

    public List<CarsDTO> getCarsByOwner(Long ownerId) {
        return carsRepository.findByIdOwner(ownerId).stream()
                .map(car -> {
                    CarsDTO dto = new CarsDTO();
                    BeanUtils.copyProperties(car, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void deleteCar(Long id) {
        if (!carsRepository.existsById(id)) {
            throw new CarNotFoundException("Voiture non trouvée avec l'ID: " + id);
        }
        carsRepository.deleteById(id);
    }

    public void updateStatCar(Long id,String stats){
        Optional<Cars> car = carsRepository.findById(id);
        if(car.isPresent()){
            Cars mycars = car.get();
            mycars.setEtatCars(stats);
            carsRepository.save(mycars);

        }else {
            throw new CarNotFoundException("cars "+id+" n'existe pas");
        }
    }
}
