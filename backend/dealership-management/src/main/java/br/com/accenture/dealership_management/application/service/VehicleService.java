package br.com.accenture.dealership_management.application.service;

import br.com.accenture.dealership_management.application.port.in.CreateVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.DeleteVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.FindVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.UploadVehicleImageUseCase;
import br.com.accenture.dealership_management.application.port.in.UpdateVehicleUseCase;
import br.com.accenture.dealership_management.application.port.out.DealershipRepositoryPort;
import br.com.accenture.dealership_management.application.port.out.ImageStoragePort;
import br.com.accenture.dealership_management.application.port.out.VehicleRepositoryPort;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class VehicleService implements CreateVehicleUseCase, FindVehicleUseCase, UpdateVehicleUseCase, DeleteVehicleUseCase, UploadVehicleImageUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final DealershipRepositoryPort dealershipRepositoryPort;
    private final ImageStoragePort imageStoragePort;

    public VehicleService(
            final VehicleRepositoryPort vehicleRepositoryPort,
            final DealershipRepositoryPort dealershipRepositoryPort,
            final ImageStoragePort imageStoragePort
    ) {
        this.vehicleRepositoryPort = vehicleRepositoryPort;
        this.dealershipRepositoryPort = dealershipRepositoryPort;
        this.imageStoragePort = imageStoragePort;
    }

    @Override
    public Vehicle create(final Vehicle vehicle) {
        if (!dealershipRepositoryPort.existsById(vehicle.getDealershipId())) {
            throw new DomainBusinessException("Operação inválida: A concessionária informada não existe.");
        }
        return vehicleRepositoryPort.save(vehicle);
    }

    @Override
    public Vehicle findById(final UUID id) {
        return vehicleRepositoryPort.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));
    }

    @Override
    public Page<Vehicle> findAll(final Pageable pageable) {
        return vehicleRepositoryPort.findAll(pageable);
    }

    @Override
    public List<Vehicle> findByDealershipId(final UUID dealershipId) {
        return vehicleRepositoryPort.findAllByDealershipId(dealershipId);
    }

    @Override
    public Page<Vehicle> search(final String brand, final String color, final Integer manufactureYear, final Pageable pageable) {
        return vehicleRepositoryPort.search(brand, color, manufactureYear, pageable);
    }

    @Override
    public Vehicle update(final UUID id, final Vehicle vehicleData) {
        if (!dealershipRepositoryPort.existsById(vehicleData.getDealershipId())) {
            throw new DomainBusinessException("Operação inválida: A concessionária informada não existe.");
        }

        final var existingVehicle = findById(id);
        final Vehicle updatedVehicle = createUpdatedVehicle(vehicleData, existingVehicle);
        updatedVehicle.assignImage(existingVehicle.getImageUrl());
        return vehicleRepositoryPort.save(updatedVehicle);
    }

    private static @NonNull Vehicle createUpdatedVehicle(Vehicle vehicleData, Vehicle existingVehicle) {
        final Vehicle updatedVehicle = new Vehicle(
                existingVehicle.getId(),
                vehicleData.getBrand(),
                vehicleData.getModel(),
                vehicleData.getFuelType(),
                vehicleData.getColor(),
                vehicleData.getDealershipId()
        );

        updatedVehicle.updateOptionalData(
                vehicleData.getManufactureYear(),
                vehicleData.getChassis(),
                vehicleData.getPrice(),
                vehicleData.getExternalColor()
        );
        return updatedVehicle;
    }

    @Override
    public void delete(final UUID id) {
        if (!vehicleRepositoryPort.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado.");
        }
        vehicleRepositoryPort.deleteById(id);
    }

    @Override
    public Vehicle uploadImage(
            final UUID vehicleId,
            final String originalFileName,
            final String contentType,
            final InputStream inputStream
    ) {
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new DomainBusinessException("Nome do arquivo da imagem é obrigatório.");
        }
        if (contentType == null || contentType.isBlank()) {
            throw new DomainBusinessException("Tipo de conteúdo da imagem é obrigatório.");
        }
        if (!contentType.startsWith("image/")) {
            throw new DomainBusinessException("Apenas arquivos de imagem sao permitidos.");
        }

        final var vehicle = findById(vehicleId);
        final String objectKey = "vehicles/" + vehicleId + "/" + UUID.randomUUID() + "-" + sanitizeFileName(originalFileName);
        final String imageUrl = imageStoragePort.uploadImage(objectKey, inputStream, contentType);

        vehicle.assignImage(imageUrl);
        return vehicleRepositoryPort.save(vehicle);
    }

    private String sanitizeFileName(final String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}