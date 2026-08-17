package br.com.accenture.dealership_management.infrastructure.adapter.out.integration;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import br.com.accenture.dealership_management.domain.model.Address;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ViaCepAdapterTest {

    @Test
    void shouldLookupAddressUsingViaCepData() {
        ViaCepClient client = mock(ViaCepClient.class);
        ViaCepAdapter adapter = new ViaCepAdapter(client);

        when(client.consultarCep("58400000"))
                .thenReturn(new ViaCepResponse("58400-000", "Rua A", "Centro", "Campina Grande", "PB", false));

        Address address = adapter.lookupByCep("58.400-000", "Fallback Rua", "Fallback Bairro", "100");

        assertEquals("58400000", address.cep().value());
        assertEquals("Rua A", address.street());
        assertEquals("Centro", address.neighborhood());
        assertEquals("Campina Grande", address.city());
    }

    @Test
    void shouldUseFallbackStreetAndNeighborhoodWhenResponseIsBlank() {
        ViaCepClient client = mock(ViaCepClient.class);
        ViaCepAdapter adapter = new ViaCepAdapter(client);

        when(client.consultarCep("58400000"))
                .thenReturn(new ViaCepResponse("58400000", " ", " ", "Joao Pessoa", "PB", false));

        Address address = adapter.lookupByCep("58400000", "Rua Fallback", "Bairro Fallback", "200");

        assertEquals("Rua Fallback", address.street());
        assertEquals("Bairro Fallback", address.neighborhood());
    }

    @Test
    void shouldThrowBusinessExceptionWhenCepNotFound() {
        ViaCepClient client = mock(ViaCepClient.class);
        ViaCepAdapter adapter = new ViaCepAdapter(client);

        when(client.consultarCep("58400000"))
                .thenReturn(new ViaCepResponse("58400000", null, null, null, null, true));

        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> adapter.lookupByCep("58400000", "Rua A", "Centro", "10"));

        assertEquals("CEP não encontrado no ViaCEP.", ex.getMessage());
    }

    @Test
    void shouldThrowGenericBusinessExceptionForUnexpectedErrors() {
        ViaCepClient client = mock(ViaCepClient.class);
        ViaCepAdapter adapter = new ViaCepAdapter(client);

        when(client.consultarCep("58400000")).thenThrow(new RuntimeException("boom"));

        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> adapter.lookupByCep("58400000", "Rua A", "Centro", "10"));

        assertEquals("Erro interno ao consultar o CEP informado.", ex.getMessage());
    }
}

