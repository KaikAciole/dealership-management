package br.com.accenture.dealership_management.domain.model;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VehicleTest {

    @Test
    void shouldCreateVehicleWhenDataIsValid() {
        Vehicle vehicle = vehicle();

        assertEquals("Fiat", vehicle.getBrand());
        assertEquals(FuelType.FLEX, vehicle.getFuelType());
        assertEquals("Azul", vehicle.getColor());
    }

    @Test
    void shouldUpdateOptionalData() {
        Vehicle vehicle = vehicle();

        vehicle.updateOptionalData(2024, "9BWZZZ377VT004251", BigDecimal.valueOf(99000), "Azul Metalico");

        assertEquals(2024, vehicle.getManufactureYear());
        assertEquals("9BWZZZ377VT004251", vehicle.getChassis());
        assertEquals(BigDecimal.valueOf(99000), vehicle.getPrice());
        assertEquals("Azul Metalico", vehicle.getExternalColor());
    }

    @Test
    void shouldFailWhenPriceIsNegative() {
        Vehicle vehicle = vehicle();

        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> vehicle.updateOptionalData(2024, "CHASSI", BigDecimal.valueOf(-1), "Azul"));

        assertEquals("O preço não pode ser negativo.", ex.getMessage());
    }

    @Test
    void shouldAssignImageAndTransferDealership() {
        Vehicle vehicle = vehicle();
        UUID newDealershipId = UUID.randomUUID();

        vehicle.assignImage("http://localhost/image.jpg");
        vehicle.transferToDealership(newDealershipId);

        assertEquals("http://localhost/image.jpg", vehicle.getImageUrl());
        assertEquals(newDealershipId, vehicle.getDealershipId());
    }

    @Test
    void shouldFailWhenTransferToNullDealership() {
        Vehicle vehicle = vehicle();

        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> vehicle.transferToDealership(null));

        assertEquals("A nova concessionária não pode ser nula.", ex.getMessage());
    }

    @Test
    void shouldValidateConstructorMandatoryFields() {
        UUID id = UUID.randomUUID();
        UUID dealershipId = UUID.randomUUID();

        DomainBusinessException idEx = assertThrows(DomainBusinessException.class,
                () -> new Vehicle(null, "Fiat", "Pulse", FuelType.FLEX, "Azul", dealershipId));
        DomainBusinessException brandEx = assertThrows(DomainBusinessException.class,
                () -> new Vehicle(id, " ", "Pulse", FuelType.FLEX, "Azul", dealershipId));
        DomainBusinessException modelEx = assertThrows(DomainBusinessException.class,
                () -> new Vehicle(id, "Fiat", " ", FuelType.FLEX, "Azul", dealershipId));
        DomainBusinessException fuelEx = assertThrows(DomainBusinessException.class,
                () -> new Vehicle(id, "Fiat", "Pulse", null, "Azul", dealershipId));
        DomainBusinessException colorEx = assertThrows(DomainBusinessException.class,
                () -> new Vehicle(id, "Fiat", "Pulse", FuelType.FLEX, " ", dealershipId));
        DomainBusinessException dealershipEx = assertThrows(DomainBusinessException.class,
                () -> new Vehicle(id, "Fiat", "Pulse", FuelType.FLEX, "Azul", null));

        assertEquals("ID do veículo é obrigatório.", idEx.getMessage());
        assertEquals("Marca é obrigatória.", brandEx.getMessage());
        assertEquals("Modelo é obrigatório.", modelEx.getMessage());
        assertEquals("Tipo de combustível é obrigatório.", fuelEx.getMessage());
        assertEquals("Cor é obrigatória.", colorEx.getMessage());
        assertEquals("O veículo deve estar associado a uma concessionária.", dealershipEx.getMessage());
    }

    private Vehicle vehicle() {
        return new Vehicle(
                UUID.randomUUID(),
                "Fiat",
                "Pulse",
                FuelType.FLEX,
                "Azul",
                UUID.randomUUID()
        );
    }
}

