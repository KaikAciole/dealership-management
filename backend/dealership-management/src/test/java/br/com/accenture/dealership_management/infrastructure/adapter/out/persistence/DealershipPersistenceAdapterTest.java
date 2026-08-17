package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence;

import br.com.accenture.dealership_management.domain.model.Address;
import br.com.accenture.dealership_management.domain.model.Cep;
import br.com.accenture.dealership_management.domain.model.Cnpj;
import br.com.accenture.dealership_management.domain.model.Dealership;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.DealershipEntity;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.mapper.DealershipMapper;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.repository.SpringDataDealershipRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealershipPersistenceAdapterTest {

    @Mock
    private SpringDataDealershipRepository repository;
    @Mock
    private DealershipMapper mapper;

    private DealershipPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new DealershipPersistenceAdapter(repository, mapper);
    }

    @Test
    void shouldSaveDealership() {
        Dealership domain = dealership();
        DealershipEntity entity = new DealershipEntity();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Dealership saved = adapter.save(domain);

        assertEquals(domain, saved);
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        Dealership domain = dealership();
        DealershipEntity entity = new DealershipEntity();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Dealership> result = adapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void shouldFindAll() {
        PageRequest pageable = PageRequest.of(0, 10);
        DealershipEntity entity = new DealershipEntity();
        Dealership domain = dealership();
        Page<DealershipEntity> page = new PageImpl<>(List.of(entity));

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Page<Dealership> result = adapter.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(domain, result.getContent().get(0));
    }

    @Test
    void shouldDelegateExistsAndDeleteOperations() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);
        when(repository.existsByCnpj("123")).thenReturn(true);

        assertTrue(adapter.existsById(id));
        assertTrue(adapter.existsByCnpj("123"));

        adapter.deleteById(id);

        verify(repository).deleteById(id);
    }

    private Dealership dealership() {
        return new Dealership(
                UUID.randomUUID(),
                "Concessionaria XPTO",
                new Cnpj("12345678000199"),
                new Address(new Cep("58400000"), "Rua A", "10", "Centro", "Campina Grande", "PB")
        );
    }
}

