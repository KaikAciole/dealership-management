package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence;

import br.com.accenture.dealership_management.application.port.out.VehicleRepositoryPort;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.mapper.VehicleMapper;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.repository.SpringDataVehicleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class VehiclePersistenceAdapter implements VehicleRepositoryPort {

    private final SpringDataVehicleRepository repository;
    private final VehicleMapper mapper;

    public VehiclePersistenceAdapter(final SpringDataVehicleRepository repository, final VehicleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Vehicle save(final Vehicle vehicle) {
        final var entity = mapper.toEntity(vehicle);
        final var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Vehicle> findById(final UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Vehicle> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(final UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return false;
    }
}