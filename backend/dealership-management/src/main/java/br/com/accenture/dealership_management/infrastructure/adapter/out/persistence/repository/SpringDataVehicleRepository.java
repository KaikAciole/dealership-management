package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.repository;

import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.VehicleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataVehicleRepository extends JpaRepository<VehicleEntity, UUID> {
    List<VehicleEntity> findAllByDealershipId(UUID dealershipId);
    boolean existsByDealershipId(UUID dealershipId);

    @Query("SELECT v FROM VehicleEntity v WHERE " +
            "(:brand IS NULL OR LOWER(v.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
            "(:color IS NULL OR LOWER(v.color) LIKE LOWER(CONCAT('%', :color, '%'))) AND " +
            "(:manufactureYear IS NULL OR v.manufactureYear = :manufactureYear)")
    Page<VehicleEntity> findByFilters(
            @Param("brand") String brand,
            @Param("color") String color,
            @Param("manufactureYear") Integer manufactureYear,
            Pageable pageable
    );
}