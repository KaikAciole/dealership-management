package br.com.accenture.dealership_management.application.port.in;

import br.com.accenture.dealership_management.domain.model.Dealership;
import java.util.UUID;

public interface UpdateDealershipUseCase {
    Dealership update(UUID id, Dealership dealership);
}