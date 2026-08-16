package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DealershipRequest(
        @NotBlank(message = "Razao social e obrigatoria")
        String corporateName,
        @NotBlank(message = "CNPJ e obrigatorio")
        String cnpj,
        @NotNull(message = "Endereco e obrigatorio")
        @Valid
        AddressRequest address
) {}