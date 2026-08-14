package br.com.accenture.dealership_management.application.service;

import br.com.accenture.dealership_management.application.port.in.CreateVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.DeleteVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.FindVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.UpdateVehicleUseCase;
import br.com.accenture.dealership_management.application.port.out.DealershipRepositoryPort;
import br.com.accenture.dealership_management.application.port.out.VehicleRepositoryPort;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleService implements
        CreateVehicleUseCase, FindVehicleUseCase, UpdateVehicleUseCase, DeleteVehicleUseCase {

    private final VehicleRepositoryPort vehicleRepositoryPort;
    private final DealershipRepositoryPort dealershipRepositoryPort;

    public VehicleService(
            final VehicleRepositoryPort vehicleRepositoryPort,
            final DealershipRepositoryPort dealershipRepositoryPort
    ) {
        this.vehicleRepositoryPort = vehicleRepositoryPort;
        this.dealershipRepositoryPort = dealershipRepositoryPort;
    }

    @Override
    public Vehicle create(final Vehicle vehicle) {
        validateDealershipExists(vehicle.getDealershipId());
        return vehicleRepositoryPort.save(vehicle);
    }

    @Override
    public Vehicle findById(final UUID id) {
        return vehicleRepositoryPort.findById(id)
                .orElseThrow(() -> new DomainBusinessException("Veículo não encontrado. ID: " + id));
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepositoryPort.findAll();
    }

    @Override
    public Vehicle update(final UUID id, final Vehicle vehicleData) {
        final Vehicle existingVehicle = findById(id);

        validateDealershipExists(vehicleData.getDealershipId());

        existingVehicle.transferToDealership(vehicleData.getDealershipId());
        existingVehicle.updateOptionalData(
                vehicleData.getManufactureYear(),
                vehicleData.getChassis(),
                vehicleData.getPrice(),
                vehicleData.getExternalColor()
        );

        return vehicleRepositoryPort.save(existingVehicle);
    }

    @Override
    public void delete(final UUID id) {
        if (!vehicleRepositoryPort.existsById(id)) {
            throw new DomainBusinessException("Não é possível deletar. Veículo não encontrado. ID: " + id);
        }
        vehicleRepositoryPort.deleteById(id);
    }

    private void validateDealershipExists(final UUID dealershipId) {
        if (!dealershipRepositoryPort.existsById(dealershipId)) {
            throw new DomainBusinessException("Operação inválida. Concessionária não encontrada. ID: " + dealershipId);
        }
    }
}