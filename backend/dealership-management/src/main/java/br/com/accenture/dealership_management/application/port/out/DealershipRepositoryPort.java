package br.com.accenture.dealership_management.application.port.out;

import br.com.accenture.dealership_management.domain.model.Dealership;
import java.util.Optional;
import java.util.UUID;

public interface DealershipRepositoryPort {
    Dealership save(Dealership dealership);
    Optional<Dealership> findById(UUID id);
}