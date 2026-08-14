package br.com.accenture.dealership_management.application.port.in;

import br.com.accenture.dealership_management.domain.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface FindVehicleUseCase {
    Vehicle findById(UUID id);
    Page<Vehicle> findAll(Pageable pageable);
    List<Vehicle> findByDealershipId(UUID dealershipId);
    Page<Vehicle> search(String brand, String color, Integer manufactureYear, Pageable pageable);
}