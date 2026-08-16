package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;

public record DealershipRequest(
        String corporateName,
        String cnpj,
        @NotNull
        AddressRequest address
) {}