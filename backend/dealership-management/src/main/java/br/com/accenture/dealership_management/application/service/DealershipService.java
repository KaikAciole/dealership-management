package br.com.accenture.dealership_management.application.service;

import br.com.accenture.dealership_management.application.port.in.CreateDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.FindDealershipUseCase;
import br.com.accenture.dealership_management.application.port.out.DealershipRepositoryPort;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import br.com.accenture.dealership_management.domain.model.Dealership;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DealershipService implements CreateDealershipUseCase, FindDealershipUseCase {

    private final DealershipRepositoryPort dealershipRepositoryPort;

    public DealershipService(final DealershipRepositoryPort dealershipRepositoryPort) {
        this.dealershipRepositoryPort = dealershipRepositoryPort;
    }

    @Override
    public Dealership create(final Dealership dealership) {
        if (dealershipRepositoryPort.existsByCnpj(dealership.getCnpj().value())) {
            throw new DomainBusinessException("Já existe uma concessionária cadastrada com este CNPJ.");
        }
        return dealershipRepositoryPort.save(dealership);
    }

    @Override
    public Dealership findById(final UUID id) {
        return dealershipRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainBusinessException("Concessionária não encontrada. ID: " + id));
    }

    @Override
    public List<Dealership> findAll() {
        return dealershipRepositoryPort.findAll();
    }
}