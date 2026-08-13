package br.com.accenture.dealership_management.application.port.out;

import br.com.accenture.dealership_management.domain.model.Vehicle;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepositoryPort {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(UUID id);
    void deleteById(UUID id);
}