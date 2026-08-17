package br.com.accenture.dealership_management.domain.model;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddressTest {

    @Test
    void shouldCreateAddressWhenDataIsValid() {
        Address address = new Address(new Cep("58000000"), "Rua A", "10", "Centro", "Joao Pessoa", "PB");

        assertEquals("58000000", address.cep().value());
        assertEquals("Rua A", address.street());
        assertEquals("10", address.number());
        assertEquals("Centro", address.neighborhood());
        assertEquals("Joao Pessoa", address.city());
        assertEquals("PB", address.state());
    }

    @Test
    void shouldFailWhenCepIsNull() {
        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> new Address(null, "Rua A", "10", "Centro", "Joao Pessoa", "PB"));

        assertEquals("CEP é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldFailWhenStreetIsBlank() {
        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> new Address(new Cep("58000000"), " ", "10", "Centro", "Joao Pessoa", "PB"));

        assertEquals("Logradouro é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldFailWhenNumberIsBlank() {
        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> new Address(new Cep("58000000"), "Rua A", " ", "Centro", "Joao Pessoa", "PB"));

        assertEquals("Número é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldFailWhenNeighborhoodIsBlank() {
        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> new Address(new Cep("58000000"), "Rua A", "10", " ", "Joao Pessoa", "PB"));

        assertEquals("Bairro é obrigatório.", ex.getMessage());
    }

    @Test
    void shouldFailWhenCityIsBlank() {
        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> new Address(new Cep("58000000"), "Rua A", "10", "Centro", " ", "PB"));

        assertEquals("Cidade é obrigatória.", ex.getMessage());
    }

    @Test
    void shouldFailWhenStateIsBlank() {
        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> new Address(new Cep("58000000"), "Rua A", "10", "Centro", "Joao Pessoa", " "));

        assertEquals("Estado é obrigatório.", ex.getMessage());
    }
}

