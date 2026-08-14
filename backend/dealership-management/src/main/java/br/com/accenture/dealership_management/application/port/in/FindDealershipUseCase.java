package br.com.accenture.dealership_management.application.port.in;

import br.com.accenture.dealership_management.domain.model.Dealership;

import java.util.List;
import java.util.UUID;

public interface FindDealershipUseCase {
    Dealership findById(UUID id);
    List<Dealership> findAll();
}
