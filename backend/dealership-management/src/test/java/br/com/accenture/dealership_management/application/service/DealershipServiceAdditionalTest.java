package br.com.accenture.dealership_management.application.service;

import br.com.accenture.dealership_management.application.port.out.AddressLookupPort;
import br.com.accenture.dealership_management.application.port.out.CompanyInfoLookupPort;
import br.com.accenture.dealership_management.application.port.out.DealershipRepositoryPort;
import br.com.accenture.dealership_management.application.port.out.VehicleRepositoryPort;
import br.com.accenture.dealership_management.domain.model.Address;
import br.com.accenture.dealership_management.domain.model.Cep;
import br.com.accenture.dealership_management.domain.model.Cnpj;
import br.com.accenture.dealership_management.domain.model.Dealership;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealershipServiceAdditionalTest {

    @Mock
    private DealershipRepositoryPort dealershipRepositoryPort;
    @Mock
    private VehicleRepositoryPort vehicleRepositoryPort;
    @Mock
    private AddressLookupPort addressLookupPort;
    @Mock
    private CompanyInfoLookupPort companyInfoLookupPort;

    private DealershipService service;

    @BeforeEach
    void setUp() {
        service = new DealershipService(dealershipRepositoryPort, vehicleRepositoryPort, addressLookupPort, companyInfoLookupPort);
    }

    @Test
    void shouldReturnPageFromFindAll() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Dealership> expected = new PageImpl<>(List.of(dealership()));
        when(dealershipRepositoryPort.findAll(pageable)).thenReturn(expected);

        Page<Dealership> result = service.findAll(pageable);

        assertSame(expected, result);
    }

    @Test
    void shouldUpdateDealershipById() {
        UUID id = UUID.randomUUID();
        Dealership existing = dealership();
        Dealership newData = new Dealership(
                UUID.randomUUID(),
                "Novo Nome",
                new Cnpj("11222333000181"),
                new Address(new Cep("58000000"), "Rua B", "20", "Centro", "Joao Pessoa", "PB")
        );

        when(dealershipRepositoryPort.findById(id)).thenReturn(Optional.of(existing));
        when(dealershipRepositoryPort.save(existing)).thenReturn(existing);

        Dealership updated = service.update(id, newData);

        assertEquals("Novo Nome", updated.getCorporateName());
        assertEquals("11222333000181", updated.getCnpj().value());
        assertEquals("Joao Pessoa", updated.getAddress().city());
        verify(dealershipRepositoryPort).save(existing);
    }

    @Test
    void shouldDeleteWhenDealershipExistsAndHasNoVehicles() {
        UUID id = UUID.randomUUID();
        when(dealershipRepositoryPort.existsById(id)).thenReturn(true);
        when(vehicleRepositoryPort.existsByDealershipId(id)).thenReturn(false);

        service.delete(id);

        verify(dealershipRepositoryPort).deleteById(id);
    }

    @Test
    void shouldFailDeleteWhenDealershipDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(dealershipRepositoryPort.existsById(id)).thenReturn(false);

        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> service.delete(id));

        verify(vehicleRepositoryPort, never()).existsByDealershipId(any());
    }

    @Test
    void shouldToggleStatusAndPersist() {
        UUID id = UUID.randomUUID();
        Dealership dealership = dealership();
        when(dealershipRepositoryPort.findById(id)).thenReturn(Optional.of(dealership));
        when(dealershipRepositoryPort.save(dealership)).thenReturn(dealership);

        Dealership updated = service.toggleStatus(id);

        assertEquals(false, updated.isActive());
        verify(dealershipRepositoryPort).save(dealership);
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

