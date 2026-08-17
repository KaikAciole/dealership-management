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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    void findByIdShouldReturnVehicleWhenExists() {
        UUID id = UUID.randomUUID();
        Vehicle vehicle = vehicle(id);
        when(vehicleRepositoryPort.findById(id)).thenReturn(Optional.of(vehicle));

        Vehicle found = service.findById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    void findByIdShouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(vehicleRepositoryPort.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.findById(id));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Veículo não encontrado.", exception.getReason());
    }

    @Test
    void findAllShouldReturnPagedVehicles() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicle> page = new PageImpl<>(List.of(vehicle()));
        when(vehicleRepositoryPort.findAll(pageable)).thenReturn(page);

        Page<Vehicle> result = service.findAll(pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByDealershipIdShouldReturnVehicleList() {
        UUID dealershipId = UUID.randomUUID();
        when(vehicleRepositoryPort.findAllByDealershipId(dealershipId)).thenReturn(List.of(vehicle()));

        List<Vehicle> result = service.findByDealershipId(dealershipId);

        assertFalse(result.isEmpty());
        verify(vehicleRepositoryPort).findAllByDealershipId(dealershipId);
    }

    @Test
    void searchShouldReturnFilteredVehicles() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicle> page = new PageImpl<>(List.of(vehicle()));
        when(vehicleRepositoryPort.search("Fiat", "Azul", 2023, pageable)).thenReturn(page);

        Page<Vehicle> result = service.search("Fiat", "Azul", 2023, pageable);

        assertFalse(result.isEmpty());
        verify(vehicleRepositoryPort).search("Fiat", "Azul", 2023, pageable);
    }

    @Test
    void updateShouldModifyAndPersistVehicleWhenValid() {
        UUID vehicleId = UUID.randomUUID();
        UUID newDealershipId = UUID.randomUUID();

        Vehicle existingVehicle = vehicle(vehicleId);
        Vehicle updateData = new Vehicle(
                vehicleId, "Honda", "Civic", FuelType.GASOLINA, "Preto", newDealershipId
        );

        when(dealershipRepositoryPort.existsById(newDealershipId)).thenReturn(true);
        when(vehicleRepositoryPort.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepositoryPort.save(any(Vehicle.class))).thenAnswer(i -> i.getArguments()[0]);

        Vehicle updated = service.update(vehicleId, updateData);

        assertEquals("Honda", updated.getBrand());
        assertEquals("Civic", updated.getModel());
        assertEquals(newDealershipId, updated.getDealershipId());
        verify(vehicleRepositoryPort).save(any(Vehicle.class));
    }

    @Test
    void updateShouldFailWhenDealershipDoesNotExist() {
        UUID id = UUID.randomUUID();
        Vehicle updateData = vehicle(id);

        when(dealershipRepositoryPort.existsById(updateData.getDealershipId())).thenReturn(false);

        DomainBusinessException exception = assertThrows(DomainBusinessException.class, () -> service.update(id, updateData));

        assertEquals("Operação inválida: A concessionária informada não existe.", exception.getMessage());
        verify(vehicleRepositoryPort, never()).findById(any(UUID.class));
        verify(vehicleRepositoryPort, never()).save(any(Vehicle.class));
    }

    @Test
    void deleteShouldRemoveVehicleWhenExists() {
        UUID id = UUID.randomUUID();
        when(vehicleRepositoryPort.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(vehicleRepositoryPort).deleteById(id);
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