package br.com.accenture.dealership_management.infrastructure.adapter.in.web.controller;

import br.com.accenture.dealership_management.application.port.in.ChangeDealershipStatusUseCase;
import br.com.accenture.dealership_management.application.port.in.CreateDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.DeleteDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.FindDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.FindVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.UpdateDealershipUseCase;
import br.com.accenture.dealership_management.domain.model.Address;
import br.com.accenture.dealership_management.domain.model.Cep;
import br.com.accenture.dealership_management.domain.model.Cnpj;
import br.com.accenture.dealership_management.domain.model.Dealership;
import br.com.accenture.dealership_management.domain.model.FuelType;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.AddressRequest;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.DealershipRequest;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response.AddressResponse;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response.DealershipResponse;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response.VehicleResponse;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper.DealershipWebMapper;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper.VehicleWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealershipControllerTest {

    @Mock
    private CreateDealershipUseCase createDealershipUseCase;
    @Mock
    private FindDealershipUseCase findDealershipUseCase;
    @Mock
    private UpdateDealershipUseCase updateDealershipUseCase;
    @Mock
    private DeleteDealershipUseCase deleteDealershipUseCase;
    @Mock
    private ChangeDealershipStatusUseCase changeDealershipStatusUseCase;
    @Mock
    private FindVehicleUseCase findVehicleUseCase;
    @Mock
    private DealershipWebMapper mapper;
    @Mock
    private VehicleWebMapper vehicleWebMapper;

    private DealershipController controller;

    @BeforeEach
    void setUp() {
        controller = new DealershipController(
                createDealershipUseCase,
                findDealershipUseCase,
                updateDealershipUseCase,
                deleteDealershipUseCase,
                changeDealershipStatusUseCase,
                findVehicleUseCase,
                mapper,
                vehicleWebMapper
        );
    }

    @Test
    void shouldCreateDealership() {
        DealershipRequest request = request();
        Dealership domain = dealership();
        DealershipResponse response = response(domain.getId());

        when(mapper.toDomain(request)).thenReturn(domain);
        when(createDealershipUseCase.create(domain)).thenReturn(domain);
        when(mapper.toResponse(domain)).thenReturn(response);

        var entity = controller.create(request);

        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        Dealership domain = dealership();
        DealershipResponse response = response(id);

        when(findDealershipUseCase.findById(id)).thenReturn(domain);
        when(mapper.toResponse(domain)).thenReturn(response);

        var entity = controller.findById(id);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void shouldFindAllDealerships() {
        PageRequest pageable = PageRequest.of(0, 10);
        Dealership domain = dealership();
        DealershipResponse response = response(domain.getId());

        when(findDealershipUseCase.findAll(pageable)).thenReturn(new PageImpl<>(List.of(domain)));
        when(mapper.toResponse(domain)).thenReturn(response);

        var entity = controller.findAll(pageable);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(1, entity.getBody().getTotalElements());
    }

    @Test
    void shouldUpdateDealership() {
        UUID id = UUID.randomUUID();
        DealershipRequest request = request();
        Dealership domain = dealership();
        DealershipResponse response = response(id);

        when(mapper.toDomain(request)).thenReturn(domain);
        when(updateDealershipUseCase.update(id, domain)).thenReturn(domain);
        when(mapper.toResponse(domain)).thenReturn(response);

        var entity = controller.update(id, request);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void shouldDeleteDealership() {
        UUID id = UUID.randomUUID();

        var entity = controller.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode());
        verify(deleteDealershipUseCase).delete(id);
    }

    @Test
    void shouldToggleStatus() {
        UUID id = UUID.randomUUID();
        Dealership domain = dealership();
        DealershipResponse response = response(id);

        when(changeDealershipStatusUseCase.toggleStatus(id)).thenReturn(domain);
        when(mapper.toResponse(domain)).thenReturn(response);

        var entity = controller.toggleStatus(id);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void shouldFindVehiclesByDealership() {
        UUID dealershipId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle(UUID.randomUUID(), "Fiat", "Pulse", FuelType.FLEX, "Azul", dealershipId);
        VehicleResponse vehicleResponse = new VehicleResponse(
                vehicle.getId(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getFuelType().name(),
                vehicle.getColor(),
                null,
                null,
                null,
                null,
                null,
                dealershipId
        );

        when(findDealershipUseCase.findById(dealershipId)).thenReturn(dealership());
        when(findVehicleUseCase.findByDealershipId(dealershipId)).thenReturn(List.of(vehicle));
        when(vehicleWebMapper.toResponse(any(Vehicle.class))).thenReturn(vehicleResponse);

        var entity = controller.findVehiclesByDealership(dealershipId);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(1, entity.getBody().size());
        assertEquals(vehicle.getId(), entity.getBody().get(0).id());
    }

    private DealershipRequest request() {
        return new DealershipRequest(
                "Concessionaria XPTO",
                "12345678000199",
                new AddressRequest("58400000", "Rua A", "10", "Campina Grande", "PB", "Centro")
        );
    }

    private Dealership dealership() {
        return new Dealership(
                UUID.randomUUID(),
                "Concessionaria XPTO",
                new Cnpj("12345678000199"),
                new Address(new Cep("58400000"), "Rua A", "10", "Centro", "Campina Grande", "PB")
        );
    }

    private DealershipResponse response(final UUID id) {
        return new DealershipResponse(
                id,
                "Concessionaria XPTO",
                "12345678000199",
                new AddressResponse("58400000", "Rua A", "10", "Centro", "Campina Grande", "PB"),
                null,
                true
        );
    }
}

