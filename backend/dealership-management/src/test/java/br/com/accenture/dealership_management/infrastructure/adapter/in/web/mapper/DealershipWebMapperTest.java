package br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper;

import br.com.accenture.dealership_management.domain.model.Address;
import br.com.accenture.dealership_management.domain.model.Cep;
import br.com.accenture.dealership_management.domain.model.Cnpj;
import br.com.accenture.dealership_management.domain.model.Dealership;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.AddressRequest;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.DealershipRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DealershipWebMapperTest {

    private final DealershipWebMapper mapper = new DealershipWebMapper();

    @Test
    void shouldMapRequestToDomain() {
        DealershipRequest request = new DealershipRequest(
                "Concessionaria Alfa",
                "12.345.678/0001-99",
                new AddressRequest("58400000", "Rua A", "100", "Campina Grande", "PB", "Centro")
        );

        Dealership domain = mapper.toDomain(request);

        assertNotNull(domain.getId());
        assertEquals("Concessionaria Alfa", domain.getCorporateName());
        assertEquals("12345678000199", domain.getCnpj().value());
        assertEquals("58400000", domain.getAddress().cep().value());
    }

    @Test
    void shouldMapDomainToResponse() {
        Dealership domain = new Dealership(
                UUID.randomUUID(),
                "Concessionaria Alfa",
                new Cnpj("12345678000199"),
                new Address(new Cep("58400000"), "Rua A", "100", "Centro", "Campina Grande", "PB")
        );
        domain.enrichWithOpenCnpjData(LocalDate.of(2015, 3, 5), true);

        var response = mapper.toResponse(domain);

        assertEquals(domain.getId(), response.id());
        assertEquals("Concessionaria Alfa", response.corporateName());
        assertEquals("12345678000199", response.cnpj());
        assertEquals("Campina Grande", response.address().city());
        assertEquals(LocalDate.of(2015, 3, 5), response.foundationDate());
    }
}

