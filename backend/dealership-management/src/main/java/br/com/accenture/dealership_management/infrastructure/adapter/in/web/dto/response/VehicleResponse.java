package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String brand,
        String model,
        String fuelType,
        String color,
        Integer manufactureYear,
        String chassis,
        BigDecimal price,
        String externalColor,
        String imageUrl,
        UUID dealershipId
) {}