package br.com.accenture.dealership_management.application.port.in;

import br.com.accenture.dealership_management.domain.model.FuelType;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateVehicleCommand(
        String brand,
        String model,
        FuelType fuelType,
        String color,
        Integer manufactureYear,
        String chassis,
        BigDecimal price,
        String externalColor,
        UUID dealershipId
) {}