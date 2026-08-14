package br.com.accenture.dealership_management.application.port.in;

import br.com.accenture.dealership_management.domain.model.Dealership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FindDealershipUseCase {
    Dealership findById(UUID id);
    Page<Dealership> findAll(Pageable pageable);
}
