package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldCreateRecordsAndExposeAccessors() {
        AddressRequest address = new AddressRequest("58400000", "Rua A", "100", "Campina Grande", "PB", "Centro");
        DealershipRequest dealership = new DealershipRequest("Concessionaria Alfa", "12345678000199", address);
        VehicleRequest vehicle = new VehicleRequest(
                "Fiat",
                "Pulse",
                "FLEX",
                "Azul",
                2024,
                "9BWZZZ377VT004251",
                BigDecimal.valueOf(100000),
                "Azul Metalico",
                UUID.randomUUID()
        );

        assertEquals("58400000", address.cep());
        assertEquals("Concessionaria Alfa", dealership.corporateName());
        assertEquals("Pulse", vehicle.model());
    }

    @Test
    void shouldValidateNestedDealershipRequestConstraints() {
        AddressRequest invalidAddress = new AddressRequest(" ", " ", " ", " ", " ", " ");
        DealershipRequest invalidRequest = new DealershipRequest(" ", " ", invalidAddress);

        Set<?> violations = validator.validate(invalidRequest);

        assertTrue(violations.size() >= 8);
    }

    @Test
    void shouldValidateVehicleRequestMandatoryFields() {
        VehicleRequest invalidVehicle = new VehicleRequest(" ", " ", " ", " ", null, null, null, null, null);

        Set<?> violations = validator.validate(invalidVehicle);

        assertTrue(violations.size() >= 5);
    }
}

