package br.com.accenture.dealership_management.application.service;

import br.com.accenture.dealership_management.application.port.out.DealershipRepositoryPort;
import br.com.accenture.dealership_management.application.port.out.ImageStoragePort;
import br.com.accenture.dealership_management.application.port.out.VehicleRepositoryPort;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import br.com.accenture.dealership_management.domain.model.FuelType;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepositoryPort vehicleRepositoryPort;
    @Mock
    private DealershipRepositoryPort dealershipRepositoryPort;
    @Mock
    private ImageStoragePort imageStoragePort;

    private VehicleService service;

    @BeforeEach
    void setUp() {
        service = new VehicleService(vehicleRepositoryPort, dealershipRepositoryPort, imageStoragePort);
    }

    @Test
    void createShouldPersistVehicleWhenDealershipExists() {
        Vehicle vehicle = vehicle();
        when(dealershipRepositoryPort.existsById(vehicle.getDealershipId())).thenReturn(true);
        when(vehicleRepositoryPort.save(vehicle)).thenReturn(vehicle);

        Vehicle saved = service.create(vehicle);

        assertEquals(vehicle.getId(), saved.getId());
        verify(vehicleRepositoryPort).save(vehicle);
    }

    @Test
    void createShouldFailWhenDealershipDoesNotExist() {
        Vehicle vehicle = vehicle();
        when(dealershipRepositoryPort.existsById(vehicle.getDealershipId())).thenReturn(false);

        DomainBusinessException exception = assertThrows(DomainBusinessException.class, () -> service.create(vehicle));

        assertEquals("Operação inválida: A concessionária informada não existe.", exception.getMessage());
        verify(vehicleRepositoryPort, never()).save(any(Vehicle.class));
    }

    @Test
    void deleteShouldFailWhenVehicleDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(vehicleRepositoryPort.existsById(id)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.delete(id));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Veículo não encontrado.", exception.getReason());
        verify(vehicleRepositoryPort, never()).deleteById(any(UUID.class));
    }

    @Test
    void uploadImageShouldPersistImageUrl() {
        UUID id = UUID.randomUUID();
        Vehicle existingVehicle = vehicle(id);
        ByteArrayInputStream inputStream = new ByteArrayInputStream("img".getBytes());

        when(vehicleRepositoryPort.findById(id)).thenReturn(Optional.of(existingVehicle));
        when(imageStoragePort.uploadImage(anyString(), any(), anyString()))
                .thenReturn("http://localhost:9000/vehicle-images/vehicles/image.jpg");
        when(vehicleRepositoryPort.save(existingVehicle)).thenReturn(existingVehicle);

        Vehicle updated = service.uploadImage(id, "photo.jpg", "image/jpeg", inputStream);

        assertEquals("http://localhost:9000/vehicle-images/vehicles/image.jpg", updated.getImageUrl());
        verify(vehicleRepositoryPort).save(existingVehicle);
    }

    @Test
    void uploadImageShouldRejectNonImageContentType() {
        DomainBusinessException exception = assertThrows(
                DomainBusinessException.class,
                () -> service.uploadImage(UUID.randomUUID(), "file.txt", "text/plain", new ByteArrayInputStream(new byte[0]))
        );

        assertEquals("Apenas arquivos de imagem sao permitidos.", exception.getMessage());
        verify(imageStoragePort, never()).uploadImage(anyString(), any(), anyString());
    }

    private Vehicle vehicle() {
        return vehicle(UUID.randomUUID());
    }

    private Vehicle vehicle(UUID id) {
        return new Vehicle(
                id,
                "Fiat",
                "Pulse",
                FuelType.FLEX,
                "Azul",
                UUID.randomUUID()
        );
    }
}

