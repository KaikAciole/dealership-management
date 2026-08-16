package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

public record AddressRequest(
        String cep,
        String street,
        String number,
        String city,
        String state,
        String neighborhood
) {}

