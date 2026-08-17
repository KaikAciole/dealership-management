package br.com.accenture.dealership_management.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FuelTypeTest {

    @Test
    void shouldExposeAllFuelTypes() {
        assertEquals(6, FuelType.values().length);
        assertEquals(FuelType.FLEX, FuelType.valueOf("FLEX"));
    }
}

