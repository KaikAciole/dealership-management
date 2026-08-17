package br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper;

import br.com.accenture.dealership_management.domain.model.FuelType;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.VehicleRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VehicleWebMapperTest {

    private final VehicleWebMapper mapper = new VehicleWebMapper();

    @Test
    void shouldMapRequestToDomain() {
        UUID dealershipId = UUID.randomUUID();
        VehicleRequest request = new VehicleRequest(
                "Fiat",
                "Pulse",
                "FLEX",
                "Azul",
                2024,
                "9BWZZZ377VT004251",
                BigDecimal.valueOf(120000),
                "Azul Metalico",
                dealershipId
        );

        Vehicle domain = mapper.toDomain(request);

        assertNotNull(domain.getId());
        assertEquals("Fiat", domain.getBrand());
        assertEquals(FuelType.FLEX, domain.getFuelType());
        assertEquals(2024, domain.getManufactureYear());
        assertEquals(dealershipId, domain.getDealershipId());
    }

    @Test
    void shouldMapDomainToResponse() {
        Vehicle vehicle = new Vehicle(
                UUID.randomUUID(),
                "Toyota",
                "Corolla",
                FuelType.GASOLINA,
                "Preto",
                UUID.randomUUID()
        );
        vehicle.updateOptionalData(2022, "9BWZZZ377VT004251", BigDecimal.valueOf(99000), "Preto Fosco");
        vehicle.assignImage("http://localhost/image.jpg");

        var response = mapper.toResponse(vehicle);

        assertEquals(vehicle.getId(), response.id());
        assertEquals("Toyota", response.brand());
        assertEquals("GASOLINA", response.fuelType());
        assertEquals("http://localhost/image.jpg", response.imageUrl());
    }
}

