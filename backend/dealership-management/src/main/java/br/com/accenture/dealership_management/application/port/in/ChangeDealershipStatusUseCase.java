package br.com.accenture.dealership_management.application.port.in;

import br.com.accenture.dealership_management.domain.model.Dealership;
import java.util.UUID;

public interface ChangeDealershipStatusUseCase {
    Dealership toggleStatus(UUID id);
}
