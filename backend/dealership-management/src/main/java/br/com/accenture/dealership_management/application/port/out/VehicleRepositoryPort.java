package br.com.accenture.dealership_management.application.port.out;

import br.com.accenture.dealership_management.domain.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepositoryPort {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(UUID id);
    Page<Vehicle> findAll(Pageable pageable);
    void deleteById(UUID id);
    boolean existsById(UUID id);
    List<Vehicle> findAllByDealershipId(UUID dealershipId);
    boolean existsByDealershipId(UUID dealershipId);
    Page<Vehicle> search(String brand, String color, Integer manufactureYear, Pageable pageable);
}