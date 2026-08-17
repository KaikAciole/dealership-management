package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.mapper;

import br.com.accenture.dealership_management.domain.model.Address;
import br.com.accenture.dealership_management.domain.model.Cep;
import br.com.accenture.dealership_management.domain.model.Cnpj;
import br.com.accenture.dealership_management.domain.model.Dealership;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.DealershipEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DealershipMapperTest {

    private final DealershipMapper mapper = new DealershipMapper();

    @Test
    void shouldMapDomainToEntity() {
        Dealership domain = new Dealership(
                UUID.randomUUID(),
                "Concessionaria Alfa",
                new Cnpj("12345678000199"),
                new Address(new Cep("58400000"), "Rua A", "100", "Centro", "Campina Grande", "PB")
        );
        domain.enrichWithOpenCnpjData(LocalDate.of(2012, 1, 10), true);

        DealershipEntity entity = mapper.toEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals("Concessionaria Alfa", entity.getCorporateName());
        assertEquals("12345678000199", entity.getCnpj());
        assertEquals(LocalDate.of(2012, 1, 10), entity.getFoundationDate());
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        DealershipEntity entity = DealershipEntity.builder()
                .id(id)
                .corporateName("Concessionaria Beta")
                .cnpj("12345678000199")
                .cep("58400000")
                .street("Rua B")
                .number("200")
                .neighborhood("Centro")
                .city("Joao Pessoa")
                .state("PB")
                .foundationDate(LocalDate.of(2018, 5, 20))
                .isActive(false)
                .build();

        Dealership domain = mapper.toDomain(entity);

        assertEquals(id, domain.getId());
        assertEquals("Concessionaria Beta", domain.getCorporateName());
        assertEquals("Joao Pessoa", domain.getAddress().city());
        assertEquals(LocalDate.of(2018, 5, 20), domain.getFoundationDate());
        assertEquals(false, domain.isActive());
    }
}

