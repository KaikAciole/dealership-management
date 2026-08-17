package br.com.accenture.dealership_management.domain.model;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CepTest {

    @Test
    void shouldNormalizeCepWhenValid() {
        Cep cep = new Cep("58.400-000");

        assertEquals("58400000", cep.value());
    }

    @Test
    void shouldFailWhenCepIsBlank() {
        DomainBusinessException ex = assertThrows(DomainBusinessException.class, () -> new Cep(" "));

        assertEquals("O CEP não pode ser nulo ou vazio.", ex.getMessage());
    }

    @Test
    void shouldFailWhenCepHasInvalidLength() {
        DomainBusinessException ex = assertThrows(DomainBusinessException.class, () -> new Cep("123"));

        assertEquals("O formato do CEP é inválido. Deve conter 8 dígitos.", ex.getMessage());
    }
}

