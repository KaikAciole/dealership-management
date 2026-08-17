package br.com.accenture.dealership_management.infrastructure.adapter.out.integration;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenCnpjAdapterTest {

    @Test
    void shouldLookupCompanyInfoSuccessfully() {
        OpenCnpjClient client = mock(OpenCnpjClient.class);
        OpenCnpjAdapter adapter = new OpenCnpjAdapter(client);

        when(client.consultarCnpj("12345678000199"))
                .thenReturn(new OpenCnpjResponse("2020-10-15", "ATIVA"));

        var info = adapter.lookupByCnpj("12.345.678/0001-99");

        assertEquals(LocalDate.of(2020, 10, 15), info.foundationDate());
        assertEquals(true, info.isActive());
    }

    @Test
    void shouldThrowBusinessExceptionWhenClientFails() {
        OpenCnpjClient client = mock(OpenCnpjClient.class);
        OpenCnpjAdapter adapter = new OpenCnpjAdapter(client);

        when(client.consultarCnpj("12345678000199")).thenThrow(new RuntimeException("boom"));

        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> adapter.lookupByCnpj("12.345.678/0001-99"));

        assertEquals("Erro ao consultar informações do CNPJ na OpenCNPJ.", ex.getMessage());
    }
}

