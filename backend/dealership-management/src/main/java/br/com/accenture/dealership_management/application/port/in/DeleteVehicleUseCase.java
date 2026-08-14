package br.com.accenture.dealership_management.application.port.in;

import java.util.UUID;

public interface DeleteVehicleUseCase {
    void delete(UUID id);
}
