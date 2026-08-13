package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.mapper;

import br.com.accenture.dealership_management.domain.model.FuelType;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.VehicleEntity;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public VehicleEntity toEntity(final Vehicle domain) {
        return VehicleEntity.builder()
                .id(domain.getId())
                .brand(domain.getBrand())
                .model(domain.getModel())
                .fuelType(domain.getFuelType().name())
                .color(domain.getColor())
                .manufactureYear(domain.getManufactureYear())
                .chassis(domain.getChassis())
                .price(domain.getPrice())
                .externalColor(domain.getExternalColor())
                .imageUrl(domain.getImageUrl())
                .dealershipId(domain.getDealershipId())
                .build();
    }

    public Vehicle toDomain(final VehicleEntity entity) {
        final Vehicle vehicle = new Vehicle(
                entity.getId(),
                entity.getBrand(),
                entity.getModel(),
                FuelType.valueOf(entity.getFuelType()),
                entity.getColor(),
                entity.getDealershipId()
        );

        vehicle.updateOptionalData(
                entity.getManufactureYear(),
                entity.getChassis(),
                entity.getPrice(),
                entity.getExternalColor()
        );
        vehicle.assignImage(entity.getImageUrl());

        return vehicle;
    }
}