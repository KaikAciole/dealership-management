package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record VehicleRequest(
        @NotBlank(message = "Marca e obrigatoria")
        String brand,
        @NotBlank(message = "Modelo e obrigatorio")
        String model,
        @NotBlank(message = "Tipo de combustivel e obrigatorio")
        String fuelType,
        @NotBlank(message = "Cor e obrigatoria")
        String color,
        Integer manufactureYear,
        String chassis,
        BigDecimal price,
        String externalColor,
        @NotNull(message = "Concessionaria e obrigatoria")
        UUID dealershipId
) {}