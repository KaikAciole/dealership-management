package br.com.accenture.dealership_management.domain.model;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DealershipTest {

    @Test
    void shouldCreateDealershipWithDefaultActiveStatus() {
        Dealership dealership = dealership();

        assertTrue(dealership.isActive());
        assertEquals("Concessionaria XPTO", dealership.getCorporateName());
    }

    @Test
    void shouldUpdateAddress() {
        Dealership dealership = dealership();
        Address newAddress = new Address(new Cep("58000000"), "Rua B", "20", "Centro", "Joao Pessoa", "PB");

        dealership.updateAddress(newAddress);

        assertEquals("Rua B", dealership.getAddress().street());
    }

    @Test
    void shouldFailWhenUpdatingAddressWithNull() {
        Dealership dealership = dealership();

        DomainBusinessException ex = assertThrows(DomainBusinessException.class, () -> dealership.updateAddress(null));

        assertEquals("O novo endereço não pode ser nulo.", ex.getMessage());
    }

    @Test
    void shouldEnrichWithCompanyData() {
        Dealership dealership = dealership();
        LocalDate foundationDate = LocalDate.of(2010, 5, 10);

        dealership.enrichWithOpenCnpjData(foundationDate, false);

        assertEquals(foundationDate, dealership.getFoundationDate());
        assertFalse(dealership.isActive());
    }

    @Test
    void shouldUpdateCoreData() {
        Dealership dealership = dealership();

        dealership.updateData(
                "Concessionaria Nova",
                new Cnpj("11222333000181"),
                new Address(new Cep("58700000"), "Rua C", "30", "Bairro C", "Patos", "PB")
        );

        assertEquals("Concessionaria Nova", dealership.getCorporateName());
        assertEquals("11222333000181", dealership.getCnpj().value());
        assertEquals("Patos", dealership.getAddress().city());
    }

    @Test
    void shouldFailWhenUpdatingCoreDataWithInvalidValues() {
        Dealership dealership = dealership();

        DomainBusinessException nameEx = assertThrows(DomainBusinessException.class,
                () -> dealership.updateData(" ", new Cnpj("12345678000199"), dealership.getAddress()));
        DomainBusinessException cnpjEx = assertThrows(DomainBusinessException.class,
                () -> dealership.updateData("Nome", null, dealership.getAddress()));
        DomainBusinessException addressEx = assertThrows(DomainBusinessException.class,
                () -> dealership.updateData("Nome", new Cnpj("12345678000199"), null));

        assertEquals("Razão social é obrigatória.", nameEx.getMessage());
        assertEquals("CNPJ é obrigatório.", cnpjEx.getMessage());
        assertEquals("Endereço é obrigatório.", addressEx.getMessage());

        DomainBusinessException nullNameEx = assertThrows(DomainBusinessException.class,
                () -> dealership.updateData(null, new Cnpj("12345678000199"), dealership.getAddress()));
        assertEquals("Razão social é obrigatória.", nullNameEx.getMessage());
    }

    @Test
    void shouldToggleActiveStatus() {
        Dealership dealership = dealership();

        dealership.toggleActive();

        assertFalse(dealership.isActive());

        dealership.toggleActive();

        assertTrue(dealership.isActive());
    }

    @Test
    void shouldValidateConstructorMandatoryFields() {
        Address validAddress = new Address(new Cep("58400000"), "Rua Teste", "10", "Centro", "Campina Grande", "PB");
        Cnpj validCnpj = new Cnpj("12345678000199");

        DomainBusinessException idEx = assertThrows(DomainBusinessException.class,
                () -> new Dealership(null, "Nome", validCnpj, validAddress));
        DomainBusinessException nameEx = assertThrows(DomainBusinessException.class,
                () -> new Dealership(UUID.randomUUID(), " ", validCnpj, validAddress));
        DomainBusinessException nullNameEx = assertThrows(DomainBusinessException.class,
                () -> new Dealership(UUID.randomUUID(), null, validCnpj, validAddress));
        DomainBusinessException cnpjEx = assertThrows(DomainBusinessException.class,
                () -> new Dealership(UUID.randomUUID(), "Nome", null, validAddress));
        DomainBusinessException addressEx = assertThrows(DomainBusinessException.class,
                () -> new Dealership(UUID.randomUUID(), "Nome", validCnpj, null));

        assertEquals("ID da concessionária é obrigatório.", idEx.getMessage());
        assertEquals("Razão social é obrigatória.", nameEx.getMessage());
        assertEquals("Razão social é obrigatória.", nullNameEx.getMessage());
        assertEquals("CNPJ é obrigatório.", cnpjEx.getMessage());
        assertEquals("Endereço é obrigatório.", addressEx.getMessage());
    }

    private Dealership dealership() {
        return new Dealership(
                UUID.randomUUID(),
                "Concessionaria XPTO",
                new Cnpj("12345678000199"),
                new Address(new Cep("58400000"), "Rua Teste", "10", "Centro", "Campina Grande", "PB")
        );
    }
}



