package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence;

import br.com.accenture.dealership_management.application.port.out.DealershipRepositoryPort;
import br.com.accenture.dealership_management.domain.model.Dealership;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.mapper.DealershipMapper;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.repository.SpringDataDealershipRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class DealershipPersistenceAdapter implements DealershipRepositoryPort {

    private final SpringDataDealershipRepository repository;
    private final DealershipMapper mapper;

    public DealershipPersistenceAdapter(final SpringDataDealershipRepository repository, final DealershipMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Dealership save(final Dealership dealership) {
        final var entity = mapper.toEntity(dealership);
        final var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Dealership> findById(final UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}