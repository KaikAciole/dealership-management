package br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request;

public record DealershipRequest(
        String corporateName,
        String cnpj,
        AddressRequest address
) {}