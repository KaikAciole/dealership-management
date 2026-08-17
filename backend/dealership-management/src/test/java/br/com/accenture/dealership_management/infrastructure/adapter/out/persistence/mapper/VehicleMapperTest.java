package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.mapper;

import br.com.accenture.dealership_management.domain.model.FuelType;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.VehicleEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VehicleMapperTest {

    private final VehicleMapper mapper = new VehicleMapper();

    @Test
    void shouldMapDomainToEntity() {
        Vehicle domain = new Vehicle(UUID.randomUUID(), "Fiat", "Pulse", FuelType.FLEX, "Azul", UUID.randomUUID());
        domain.updateOptionalData(2024, "9BWZZZ377VT004251", BigDecimal.valueOf(110000), "Azul Metalico");
        domain.assignImage("http://localhost/image.jpg");

        VehicleEntity entity = mapper.toEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals("Fiat", entity.getBrand());
        assertEquals("FLEX", entity.getFuelType());
        assertEquals("http://localhost/image.jpg", entity.getImageUrl());
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        UUID dealershipId = UUID.randomUUID();

        VehicleEntity entity = VehicleEntity.builder()
                .id(id)
                .brand("Toyota")
                .model("Yaris")
                .fuelType("GASOLINA")
                .color("Branco")
                .manufactureYear(2023)
                .chassis("9BWZZZ377VT004251")
                .price(BigDecimal.valueOf(90000))
                .externalColor("Branco Perola")
                .imageUrl("http://localhost/car.jpg")
                .dealershipId(dealershipId)
                .build();

        Vehicle domain = mapper.toDomain(entity);

        assertEquals(id, domain.getId());
        assertEquals("Toyota", domain.getBrand());
        assertEquals(FuelType.GASOLINA, domain.getFuelType());
        assertEquals(BigDecimal.valueOf(90000), domain.getPrice());
        assertEquals("http://localhost/car.jpg", domain.getImageUrl());
        assertEquals(dealershipId, domain.getDealershipId());
    }
}

