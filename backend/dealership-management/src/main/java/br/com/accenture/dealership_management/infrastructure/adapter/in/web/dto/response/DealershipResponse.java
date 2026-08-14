package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record DealershipResponse(
        UUID id,
        String corporateName,
        String cnpj,
        String cep,
        String street,
        String neighborhood,
        String city,
        String state,
        LocalDate foundationDate,
        boolean isActive
) {}