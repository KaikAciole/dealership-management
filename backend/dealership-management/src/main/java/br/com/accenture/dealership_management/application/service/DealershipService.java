package br.com.accenture.dealership_management.application.service;

import br.com.accenture.dealership_management.application.port.in.CreateDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.DeleteDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.FindDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.UpdateDealershipUseCase;
import br.com.accenture.dealership_management.application.port.out.DealershipRepositoryPort;
import br.com.accenture.dealership_management.application.port.out.VehicleRepositoryPort;
import br.com.accenture.dealership_management.domain.model.Dealership;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DealershipService implements CreateDealershipUseCase, FindDealershipUseCase, UpdateDealershipUseCase, DeleteDealershipUseCase {

    private final DealershipRepositoryPort dealershipRepositoryPort;
    private final VehicleRepositoryPort vehicleRepositoryPort;

    public DealershipService(final DealershipRepositoryPort dealershipRepositoryPort, final VehicleRepositoryPort vehicleRepositoryPort) {
        this.dealershipRepositoryPort = dealershipRepositoryPort;
        this.vehicleRepositoryPort = vehicleRepositoryPort;
    }

    @Override
    public Dealership create(final Dealership dealership) {
        if (dealershipRepositoryPort.existsByCnpj(dealership.getCnpj().value())) {
            throw new RuntimeException("Já existe uma concessionária com este CNPJ.");
        }
        return dealershipRepositoryPort.save(dealership);
    }

    @Override
    public Dealership findById(final UUID id) {
        return dealershipRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Concessionária não encontrada."));
    }

    @Override
    public List<Dealership> findAll() {
        return dealershipRepositoryPort.findAll();
    }

    @Override
    public Dealership update(final UUID id, final Dealership dealershipData) {
        final var existingDealership = findById(id);

        existingDealership.updateData(
                dealershipData.getCorporateName(),
                dealershipData.getCnpj(),
                dealershipData.getAddress()
        );

        return dealershipRepositoryPort.save(existingDealership);
    }

    @Override
    public void delete(final UUID id) {
        if (!dealershipRepositoryPort.existsById(id)) {
            throw new RuntimeException("Concessionária não encontrada.");
        }

        if (vehicleRepositoryPort.existsByDealershipId(id)) {
            throw new RuntimeException("Não é possível excluir uma concessionária que possui veículos vinculados.");
        }

        dealershipRepositoryPort.deleteById(id);
    }
}