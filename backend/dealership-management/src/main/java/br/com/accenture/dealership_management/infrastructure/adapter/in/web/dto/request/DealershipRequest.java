package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DealershipRequest(
        @NotBlank(message = "Razão social é obrigatória")
        String corporateName,
        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,
        @NotNull(message = "Endereco e obrigatorio")
        @Valid
        AddressRequest address
) {}