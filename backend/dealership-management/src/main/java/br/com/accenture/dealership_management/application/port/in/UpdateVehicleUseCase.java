package br.com.accenture.dealership_management.application.port.in;
import br.com.accenture.dealership_management.domain.model.Vehicle;

import java.util.UUID;

public interface UpdateVehicleUseCase {
    Vehicle update(UUID id, Vehicle vehicle);
}
