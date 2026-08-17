package br.com.accenture.dealership_management.domain.model;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CnpjTest {

    @Test
    void shouldNormalizeCnpjWhenValid() {
        Cnpj cnpj = new Cnpj("12.345.678/0001-99");

        assertEquals("12345678000199", cnpj.value());
    }

    @Test
    void shouldFailWhenCnpjIsBlank() {
        DomainBusinessException ex = assertThrows(DomainBusinessException.class, () -> new Cnpj(" "));

        assertEquals("O CNPJ não pode ser nulo ou vazio.", ex.getMessage());
    }

    @Test
    void shouldFailWhenCnpjHasInvalidLength() {
        DomainBusinessException ex = assertThrows(DomainBusinessException.class, () -> new Cnpj("123"));

        assertEquals("O formato do CNPJ é inválido. Deve conter 14 dígitos.", ex.getMessage());
    }
}

