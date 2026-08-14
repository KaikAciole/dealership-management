package br.com.accenture.dealership_management.application.port.out;

import br.com.accenture.dealership_management.domain.model.Dealership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface DealershipRepositoryPort {
    Dealership save(Dealership dealership);
    Optional<Dealership> findById(UUID id);
    Page<Dealership> findAll(Pageable pageable);
    boolean existsById(UUID id);
    boolean existsByCnpj(String cnpj);
    void deleteById(UUID id);
}