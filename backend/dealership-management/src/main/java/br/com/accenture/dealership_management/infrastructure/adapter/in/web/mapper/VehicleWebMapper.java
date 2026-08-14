package br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper;

import br.com.accenture.dealership_management.domain.model.FuelType;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.VehicleRequest;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response.VehicleResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VehicleWebMapper {

    public Vehicle toDomain(final VehicleRequest request) {
        final Vehicle vehicle = new Vehicle(
                UUID.randomUUID(),
                request.brand(),
                request.model(),
                FuelType.valueOf(request.fuelType()),
                request.color(),
                request.dealershipId()
        );

        vehicle.updateOptionalData(
                request.manufactureYear(),
                request.chassis(),
                request.price(),
                request.externalColor()
        );

        return vehicle;
    }

    public VehicleResponse toResponse(final Vehicle domain) {
        return new VehicleResponse(
                domain.getId(),
                domain.getBrand(),
                domain.getModel(),
                domain.getFuelType().name(),
                domain.getColor(),
                domain.getManufactureYear(),
                domain.getChassis(),
                domain.getPrice(),
                domain.getExternalColor(),
                domain.getImageUrl(),
                domain.getDealershipId()
        );
    }
}