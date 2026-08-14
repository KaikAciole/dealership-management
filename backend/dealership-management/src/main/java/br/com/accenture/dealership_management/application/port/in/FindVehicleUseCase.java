package br.com.accenture.dealership_management.application.port.in;

import br.com.accenture.dealership_management.domain.model.Vehicle;
import java.util.List;
import java.util.UUID;

public interface FindVehicleUseCase {
    Vehicle findById(UUID id);
    List<Vehicle> findAll();
    List<Vehicle> findByDealershipId(UUID dealershipId);
}