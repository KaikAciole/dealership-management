package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public record VehicleRequest(
        String brand,
        String model,
        String fuelType,
        String color,
        Integer manufactureYear,
        String chassis,
        BigDecimal price,
        String externalColor,
        UUID dealershipId
) {}