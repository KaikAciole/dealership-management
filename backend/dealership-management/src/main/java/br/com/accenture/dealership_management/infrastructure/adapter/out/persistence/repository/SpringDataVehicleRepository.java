package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.repository;

import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataVehicleRepository extends JpaRepository<VehicleEntity, UUID> {
    List<VehicleEntity> findAllByDealershipId(UUID dealershipId);
    boolean existsByDealershipId(UUID dealershipId);
}