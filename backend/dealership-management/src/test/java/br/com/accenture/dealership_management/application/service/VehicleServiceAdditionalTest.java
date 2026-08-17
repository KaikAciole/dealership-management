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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceAdditionalTest {

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
    void shouldFindVehicleById() {
        UUID id = UUID.randomUUID();
        Vehicle vehicle = vehicle(id);
        when(vehicleRepositoryPort.findById(id)).thenReturn(Optional.of(vehicle));

        Vehicle result = service.findById(id);

        assertSame(vehicle, result);
    }

    @Test
    void shouldReturnPageAndSearchResults() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Vehicle> expectedPage = new PageImpl<>(List.of(vehicle(UUID.randomUUID())));

        when(vehicleRepositoryPort.findAll(pageable)).thenReturn(expectedPage);
        when(vehicleRepositoryPort.search("fi", "azul", 2024, pageable)).thenReturn(expectedPage);

        assertSame(expectedPage, service.findAll(pageable));
        assertSame(expectedPage, service.search("fi", "azul", 2024, pageable));
    }

    @Test
    void shouldReturnVehiclesByDealershipId() {
        UUID dealershipId = UUID.randomUUID();
        List<Vehicle> expected = List.of(vehicle(UUID.randomUUID()));
        when(vehicleRepositoryPort.findAllByDealershipId(dealershipId)).thenReturn(expected);

        List<Vehicle> result = service.findByDealershipId(dealershipId);

        assertSame(expected, result);
    }

    @Test
    void shouldUpdateVehicleWhenDealershipExists() {
        UUID id = UUID.randomUUID();
        Vehicle existing = vehicle(id);
        Vehicle data = vehicle(UUID.randomUUID());
        data.updateOptionalData(2024, "9BWZZZ377VT004251", java.math.BigDecimal.valueOf(120000), "Azul Metalico");

        when(dealershipRepositoryPort.existsById(data.getDealershipId())).thenReturn(true);
        when(vehicleRepositoryPort.findById(id)).thenReturn(Optional.of(existing));

        when(vehicleRepositoryPort.save(any(Vehicle.class))).thenAnswer(i -> i.getArguments()[0]);

        Vehicle updated = service.update(id, data);

        assertEquals(2024, updated.getManufactureYear());
        assertEquals("9BWZZZ377VT004251", updated.getChassis());
        verify(vehicleRepositoryPort).save(any(Vehicle.class));
    }

    @Test
    void shouldFailUpdateWhenTargetDealershipDoesNotExist() {
        UUID id = UUID.randomUUID();
        Vehicle data = vehicle(UUID.randomUUID());
        when(dealershipRepositoryPort.existsById(data.getDealershipId())).thenReturn(false);

        DomainBusinessException ex = assertThrows(DomainBusinessException.class, () -> service.update(id, data));

        assertEquals("Operação inválida: A concessionária informada não existe.", ex.getMessage());
    }

    @Test
    void shouldDeleteWhenVehicleExists() {
        UUID id = UUID.randomUUID();
        when(vehicleRepositoryPort.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(vehicleRepositoryPort).deleteById(id);
    }

    @Test
    void shouldValidateUploadImageInput() {
        UUID id = UUID.randomUUID();

        DomainBusinessException fileNameEx = assertThrows(DomainBusinessException.class,
                () -> service.uploadImage(id, " ", "image/jpeg", new ByteArrayInputStream(new byte[0])));
        DomainBusinessException contentTypeEx = assertThrows(DomainBusinessException.class,
                () -> service.uploadImage(id, "file.jpg", " ", new ByteArrayInputStream(new byte[0])));

        assertEquals("Nome do arquivo da imagem é obrigatório.", fileNameEx.getMessage());
        assertEquals("Tipo de conteúdo da imagem é obrigatório.", contentTypeEx.getMessage());
    }

    @Test
    void shouldSanitizeFileNameAndPersistUploadedImage() {
        UUID id = UUID.randomUUID();
        Vehicle vehicle = vehicle(id);
        ByteArrayInputStream stream = new ByteArrayInputStream("img".getBytes(StandardCharsets.UTF_8));

        when(vehicleRepositoryPort.findById(id)).thenReturn(Optional.of(vehicle));
        when(imageStoragePort.uploadImage(anyString(), any(), anyString())).thenReturn("http://localhost/image.jpg");
        when(vehicleRepositoryPort.save(vehicle)).thenReturn(vehicle);

        Vehicle updated = service.uploadImage(id, "foto final!.jpg", "image/jpeg", stream);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        verify(imageStoragePort).uploadImage(keyCaptor.capture(), any(), anyString());

        assertEquals("http://localhost/image.jpg", updated.getImageUrl());
        String key = keyCaptor.getValue();
        assertTrue(key.contains("foto_final_.jpg"));
        assertTrue(key.startsWith("vehicles/" + id + "/"));
    }

    private Vehicle vehicle(final UUID id) {
        return new Vehicle(id, "Fiat", "Pulse", FuelType.FLEX, "Azul", UUID.randomUUID());
    }
}