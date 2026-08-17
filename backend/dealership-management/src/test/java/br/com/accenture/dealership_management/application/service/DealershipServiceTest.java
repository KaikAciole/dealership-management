package br.com.accenture.dealership_management.application.service;

import br.com.accenture.dealership_management.application.port.out.AddressLookupPort;
import br.com.accenture.dealership_management.application.port.out.CompanyInfoLookupPort;
import br.com.accenture.dealership_management.application.port.out.DealershipRepositoryPort;
import br.com.accenture.dealership_management.application.port.out.VehicleRepositoryPort;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import br.com.accenture.dealership_management.domain.model.Address;
import br.com.accenture.dealership_management.domain.model.Cep;
import br.com.accenture.dealership_management.domain.model.Cnpj;
import br.com.accenture.dealership_management.domain.model.Dealership;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealershipServiceTest {

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
        service = new DealershipService(
                dealershipRepositoryPort,
                vehicleRepositoryPort,
                addressLookupPort,
                companyInfoLookupPort
        );
    }

    @Test
    void createShouldPersistDealershipWhenDataIsValid() {
        Dealership dealership = dealership();
        Address enrichedAddress = new Address(new Cep("58000000"), "Rua A", "123", "Centro", "Campina Grande", "PB");

        when(dealershipRepositoryPort.existsByCnpj(anyString())).thenReturn(false);
        when(addressLookupPort.lookupByCep(anyString(), anyString(), anyString(), anyString())).thenReturn(enrichedAddress);
        when(companyInfoLookupPort.lookupByCnpj(anyString()))
                .thenReturn(new CompanyInfoLookupPort.CompanyInfo(LocalDate.of(2019, 1, 1), true));
        when(dealershipRepositoryPort.save(any(Dealership.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Dealership saved = service.create(dealership);

        assertEquals("Rua A", saved.getAddress().street());
        assertEquals(LocalDate.of(2019, 1, 1), saved.getFoundationDate());
        verify(dealershipRepositoryPort).save(any(Dealership.class));
    }

    @Test
    void createShouldFailWhenCnpjAlreadyExists() {
        when(dealershipRepositoryPort.existsByCnpj(anyString())).thenReturn(true);

        DomainBusinessException exception = assertThrows(
                DomainBusinessException.class,
                () -> service.create(dealership())
        );

        assertEquals("Já existe uma concessionária com este CNPJ.", exception.getMessage());
        verify(dealershipRepositoryPort, never()).save(any(Dealership.class));
    }

    @Test
    void findByIdShouldFailWhenNotFound() {
        when(dealershipRepositoryPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.findById(UUID.randomUUID())
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Concessionária não encontrada.", exception.getReason());
    }

    @Test
    void deleteShouldFailWhenDealershipHasLinkedVehicles() {
        UUID id = UUID.randomUUID();
        when(dealershipRepositoryPort.existsById(id)).thenReturn(true);
        when(vehicleRepositoryPort.existsByDealershipId(id)).thenReturn(true);

        DomainBusinessException exception = assertThrows(
                DomainBusinessException.class,
                () -> service.delete(id)
        );

        assertEquals("Não é possível excluir uma concessionária que possui veículos vinculados.", exception.getMessage());
        verify(dealershipRepositoryPort, never()).deleteById(any(UUID.class));
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

