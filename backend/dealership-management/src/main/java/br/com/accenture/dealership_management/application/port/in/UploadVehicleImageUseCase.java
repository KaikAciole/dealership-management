package br.com.accenture.dealership_management.application.port.in;

import br.com.accenture.dealership_management.domain.model.Vehicle;

import java.io.InputStream;
import java.util.UUID;

public interface UploadVehicleImageUseCase {
    Vehicle uploadImage(UUID vehicleId, String originalFileName, String contentType, InputStream inputStream);
}

