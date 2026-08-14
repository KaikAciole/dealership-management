package br.com.accenture.dealership_management.application.port.in;

import br.com.accenture.dealership_management.domain.model.Vehicle;

public interface CreateVehicleUseCase {
    Vehicle create(Vehicle vehicle);
}