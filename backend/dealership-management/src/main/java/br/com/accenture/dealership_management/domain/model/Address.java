package br.com.accenture.dealership_management.domain.model;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;

public record Address(
        Cep cep,
        String street,
        String number,
        String neighborhood,
        String city,
        String state
) {
    public Address {
        if (cep == null) throw new DomainBusinessException("CEP é obrigatório.");
        if (street == null || street.isBlank()) throw new DomainBusinessException("Logradouro é obrigatório.");
        if (number == null || number.isBlank()) throw new DomainBusinessException("Número é obrigatório.");
        if (neighborhood == null || neighborhood.isBlank()) throw new DomainBusinessException("Bairro é obrigatório.");
        if (city == null || city.isBlank()) throw new DomainBusinessException("Cidade é obrigatória.");
        if (state == null || state.isBlank()) throw new DomainBusinessException("Estado é obrigatório.");
    }
}