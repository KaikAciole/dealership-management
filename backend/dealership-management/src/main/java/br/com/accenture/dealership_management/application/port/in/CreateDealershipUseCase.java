package br.com.accenture.dealership_management.application.port.in;

import br.com.accenture.dealership_management.domain.model.Dealership;

public interface CreateDealershipUseCase {
    Dealership create(Dealership dealership);
}
