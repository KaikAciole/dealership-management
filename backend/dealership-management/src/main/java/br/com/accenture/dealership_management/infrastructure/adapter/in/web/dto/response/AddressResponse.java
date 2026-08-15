package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response;

public record AddressResponse(
        String cep,
        String street,
        String number,
        String neighborhood,
        String city,
        String state
) {}