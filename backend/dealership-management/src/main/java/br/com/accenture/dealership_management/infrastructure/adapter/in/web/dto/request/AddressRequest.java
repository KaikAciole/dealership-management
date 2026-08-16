package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank(message = "CEP é obrigatório")
        String cep,
        @NotBlank(message = "Rua é obrigatória")
        String street,
        @NotBlank(message = "Número é obrigatório")
        String number,
        @NotBlank(message = "Cidade e obrigatoria")
        String city,
        @NotBlank(message = "UF e obrigatoria")
        String state,
        @NotBlank(message = "Bairro e obrigatorio")
        String neighborhood
) {}

