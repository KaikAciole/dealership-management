package br.com.accenture.dealership_management.infrastructure.adapter.in.web.controller;

import br.com.accenture.dealership_management.application.port.in.CreateVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.DeleteVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.FindVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.UpdateVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.UploadVehicleImageUseCase;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import br.com.accenture.dealership_management.domain.model.FuelType;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.VehicleRequest;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response.VehicleResponse;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper.VehicleWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private CreateVehicleUseCase createVehicleUseCase;
    @Mock
    private FindVehicleUseCase findVehicleUseCase;
    @Mock
    private UpdateVehicleUseCase updateVehicleUseCase;
    @Mock
    private DeleteVehicleUseCase deleteVehicleUseCase;
    @Mock
    private UploadVehicleImageUseCase uploadVehicleImageUseCase;
    @Mock
    private VehicleWebMapper mapper;

    @Mock
    private MultipartFile multipartFile;

    private VehicleController controller;

    @BeforeEach
    void setUp() {
        controller = new VehicleController(
                createVehicleUseCase,
                findVehicleUseCase,
                updateVehicleUseCase,
                deleteVehicleUseCase,
                uploadVehicleImageUseCase,
                mapper
        );
    }

    @Test
    void shouldCreateVehicle() {
        VehicleRequest request = request();
        Vehicle domain = vehicle();
        VehicleResponse response = response(domain);

        when(mapper.toDomain(request)).thenReturn(domain);
        when(createVehicleUseCase.create(domain)).thenReturn(domain);
        when(mapper.toResponse(domain)).thenReturn(response);

        var entity = controller.create(request);

        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        Vehicle domain = vehicle();
        VehicleResponse response = response(domain);

        when(findVehicleUseCase.findById(id)).thenReturn(domain);
        when(mapper.toResponse(domain)).thenReturn(response);

        var entity = controller.findById(id);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void shouldFindAllVehicles() {
        PageRequest pageable = PageRequest.of(0, 10);
        Vehicle domain = vehicle();
        when(findVehicleUseCase.findAll(pageable)).thenReturn(new PageImpl<>(List.of(domain)));
        when(mapper.toResponse(domain)).thenReturn(response(domain));

        var entity = controller.findAll(pageable);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(1, entity.getBody().getTotalElements());
    }

    @Test
    void shouldSearchVehicles() {
        PageRequest pageable = PageRequest.of(0, 10);
        Vehicle domain = vehicle();
        when(findVehicleUseCase.search("fi", "azul", 2024, pageable)).thenReturn(new PageImpl<>(List.of(domain)));
        when(mapper.toResponse(domain)).thenReturn(response(domain));

        var entity = controller.search("fi", "azul", 2024, pageable);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(1, entity.getBody().getTotalElements());
    }

    @Test
    void shouldUpdateVehicle() {
        UUID id = UUID.randomUUID();
        VehicleRequest request = request();
        Vehicle domain = vehicle();
        VehicleResponse response = response(domain);

        when(mapper.toDomain(request)).thenReturn(domain);
        when(updateVehicleUseCase.update(id, domain)).thenReturn(domain);
        when(mapper.toResponse(domain)).thenReturn(response);

        var entity = controller.update(id, request);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void shouldDeleteVehicle() {
        UUID id = UUID.randomUUID();

        var entity = controller.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode());
        verify(deleteVehicleUseCase).delete(id);
    }

    @Test
    void shouldUploadImage() throws IOException {
        UUID id = UUID.randomUUID();
        Vehicle domain = vehicle();
        VehicleResponse response = response(domain);
        InputStream inputStream = new ByteArrayInputStream("img".getBytes());

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getOriginalFilename()).thenReturn("foto.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(uploadVehicleImageUseCase.uploadImage(eq(id), eq("foto.jpg"), eq("image/jpeg"), any(InputStream.class))).thenReturn(domain);
        when(mapper.toResponse(domain)).thenReturn(response);

        var entity = controller.uploadImage(id, multipartFile);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void shouldFailUploadImageWhenFileIsEmpty() {
        UUID id = UUID.randomUUID();
        when(multipartFile.isEmpty()).thenReturn(true);

        DomainBusinessException ex = assertThrows(DomainBusinessException.class, () -> controller.uploadImage(id, multipartFile));

        assertEquals("O arquivo da imagem nao pode estar vazio.", ex.getMessage());
    }

    @Test
    void shouldFailUploadImageWhenReadingFileFails() throws IOException {
        UUID id = UUID.randomUUID();
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getInputStream()).thenThrow(new IOException("boom"));

        DomainBusinessException ex = assertThrows(DomainBusinessException.class, () -> controller.uploadImage(id, multipartFile));

        assertEquals("Falha ao processar o arquivo da imagem.", ex.getMessage());
    }

    private Vehicle vehicle() {
        Vehicle vehicle = new Vehicle(UUID.randomUUID(), "Fiat", "Pulse", FuelType.FLEX, "Azul", UUID.randomUUID());
        vehicle.updateOptionalData(2024, "9BWZZZ377VT004251", BigDecimal.valueOf(99000), "Azul Metalico");
        return vehicle;
    }

    private VehicleRequest request() {
        return new VehicleRequest(
                "Fiat",
                "Pulse",
                "FLEX",
                "Azul",
                2024,
                "9BWZZZ377VT004251",
                BigDecimal.valueOf(99000),
                "Azul Metalico",
                UUID.randomUUID()
        );
    }

    private VehicleResponse response(final Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getFuelType().name(),
                vehicle.getColor(),
                vehicle.getManufactureYear(),
                vehicle.getChassis(),
                vehicle.getPrice(),
                vehicle.getExternalColor(),
                vehicle.getImageUrl(),
                vehicle.getDealershipId()
        );
    }
}

