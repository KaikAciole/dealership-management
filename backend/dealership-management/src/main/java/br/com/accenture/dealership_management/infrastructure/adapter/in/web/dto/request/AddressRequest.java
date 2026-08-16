package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank(message = "CEP e obrigatorio")
        String cep,
        @NotBlank(message = "Rua e obrigatoria")
        String street,
        @NotBlank(message = "Numero e obrigatorio")
        String number,
        @NotBlank(message = "Cidade e obrigatoria")
        String city,
        @NotBlank(message = "UF e obrigatoria")
        String state,
        @NotBlank(message = "Bairro e obrigatorio")
        String neighborhood
) {}

