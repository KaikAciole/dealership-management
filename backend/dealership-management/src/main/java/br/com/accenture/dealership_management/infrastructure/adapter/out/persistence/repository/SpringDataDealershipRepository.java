package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.repository;

import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.DealershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataDealershipRepository extends JpaRepository<DealershipEntity, UUID> {
}