package br.com.accenture.dealership_management.application.service;

import br.com.accenture.dealership_management.application.port.in.ChangeDealershipStatusUseCase;
import br.com.accenture.dealership_management.application.port.in.CreateDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.DeleteDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.FindDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.UpdateDealershipUseCase;
import br.com.accenture.dealership_management.application.port.out.AddressLookupPort;
import br.com.accenture.dealership_management.application.port.out.CompanyInfoLookupPort;
import br.com.accenture.dealership_management.application.port.out.DealershipRepositoryPort;
import br.com.accenture.dealership_management.application.port.out.VehicleRepositoryPort;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import br.com.accenture.dealership_management.domain.model.Dealership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DealershipService implements CreateDealershipUseCase, FindDealershipUseCase, UpdateDealershipUseCase, DeleteDealershipUseCase, ChangeDealershipStatusUseCase {

    private final DealershipRepositoryPort dealershipRepositoryPort;
    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final AddressLookupPort addressLookupPort;
    private final CompanyInfoLookupPort companyInfoLookupPort;

    public DealershipService(
            final DealershipRepositoryPort dealershipRepositoryPort,
            final VehicleRepositoryPort vehicleRepositoryPort,
            final AddressLookupPort addressLookupPort,
            final CompanyInfoLookupPort companyInfoLookupPort
    ) {
        this.dealershipRepositoryPort = dealershipRepositoryPort;
        this.vehicleRepositoryPort = vehicleRepositoryPort;
        this.addressLookupPort = addressLookupPort;
        this.companyInfoLookupPort = companyInfoLookupPort;
    }

    @Override
    public Dealership create(final Dealership dealership) {
        if (dealershipRepositoryPort.existsByCnpj(dealership.getCnpj().value())) {
            throw new DomainBusinessException("Já existe uma concessionária com este CNPJ.");
        }

        final var address = addressLookupPort.lookupByCep(
                dealership.getAddress().cep().value(),
                dealership.getAddress().street(),
                dealership.getAddress().neighborhood(),
                dealership.getAddress().number()
        );
        dealership.updateAddress(address);

        final var companyInfo = companyInfoLookupPort.lookupByCnpj(dealership.getCnpj().value());
        dealership.enrichWithOpenCnpjData(companyInfo.foundationDate(), companyInfo.isActive());

        return dealershipRepositoryPort.save(dealership);
    }

    @Override
    public Dealership findById(final UUID id) {
        return dealershipRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainBusinessException("Concessionária não encontrada."));
    }

    @Override
    public Page<Dealership> findAll(final Pageable pageable) {
        return dealershipRepositoryPort.findAll(pageable);
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
            throw new DomainBusinessException("Concessionária não encontrada.");
        }

        if (vehicleRepositoryPort.existsByDealershipId(id)) {
            throw new DomainBusinessException("Não é possível excluir uma concessionária que possui veículos vinculados.");
        }

        dealershipRepositoryPort.deleteById(id);
    }

    @Override
    public Dealership toggleStatus(final UUID id) {
        final var dealership = findById(id);
        dealership.toggleActive();
        return dealershipRepositoryPort.save(dealership);
    }
}