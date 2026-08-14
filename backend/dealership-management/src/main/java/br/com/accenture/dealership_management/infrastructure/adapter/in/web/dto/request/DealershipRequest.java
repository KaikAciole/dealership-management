package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

import java.time.LocalDate;

public record DealershipRequest(
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