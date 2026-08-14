package br.com.accenture.dealership_management.infrastructure.adapter.in.web.controller;

import br.com.accenture.dealership_management.application.port.in.ChangeDealershipStatusUseCase;
import br.com.accenture.dealership_management.application.port.in.CreateDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.DeleteDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.FindDealershipUseCase;
import br.com.accenture.dealership_management.application.port.in.FindVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.UpdateDealershipUseCase;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.DealershipRequest;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response.DealershipResponse;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response.VehicleResponse;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper.DealershipWebMapper;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper.VehicleWebMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dealerships")
public class DealershipController {

    private final CreateDealershipUseCase createDealershipUseCase;
    private final FindDealershipUseCase findDealershipUseCase;
    private final UpdateDealershipUseCase updateDealershipUseCase;
    private final DeleteDealershipUseCase deleteDealershipUseCase;
    private final ChangeDealershipStatusUseCase changeDealershipStatusUseCase;
    private final FindVehicleUseCase findVehicleUseCase;
    private final DealershipWebMapper mapper;
    private final VehicleWebMapper vehicleWebMapper;

    public DealershipController(
            final CreateDealershipUseCase createDealershipUseCase,
            final FindDealershipUseCase findDealershipUseCase,
            final UpdateDealershipUseCase updateDealershipUseCase,
            final DeleteDealershipUseCase deleteDealershipUseCase,
            final ChangeDealershipStatusUseCase changeDealershipStatusUseCase,
            final FindVehicleUseCase findVehicleUseCase,
            final DealershipWebMapper mapper,
            final VehicleWebMapper vehicleWebMapper
    ) {
        this.createDealershipUseCase = createDealershipUseCase;
        this.findDealershipUseCase = findDealershipUseCase;
        this.updateDealershipUseCase = updateDealershipUseCase;
        this.deleteDealershipUseCase = deleteDealershipUseCase;
        this.changeDealershipStatusUseCase = changeDealershipStatusUseCase;
        this.findVehicleUseCase = findVehicleUseCase;
        this.mapper = mapper;
        this.vehicleWebMapper = vehicleWebMapper;
    }

    @PostMapping
    public ResponseEntity<DealershipResponse> create(@RequestBody final DealershipRequest request) {
        final var domain = mapper.toDomain(request);
        final var createdDealership = createDealershipUseCase.create(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(createdDealership));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealershipResponse> findById(@PathVariable final UUID id) {
        final var dealership = findDealershipUseCase.findById(id);
        return ResponseEntity.ok(mapper.toResponse(dealership));
    }

    @GetMapping
    public ResponseEntity<Page<DealershipResponse>> findAll(final Pageable pageable) {
        final var page = findDealershipUseCase.findAll(pageable);
        return ResponseEntity.ok(page.map(mapper::toResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DealershipResponse> update(@PathVariable final UUID id, @RequestBody final DealershipRequest request) {
        final var domainData = mapper.toDomain(request);
        final var updatedDealership = updateDealershipUseCase.update(id, domainData);
        return ResponseEntity.ok(mapper.toResponse(updatedDealership));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final UUID id) {
        deleteDealershipUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DealershipResponse> toggleStatus(@PathVariable final UUID id) {
        final var dealership = changeDealershipStatusUseCase.toggleStatus(id);
        return ResponseEntity.ok(mapper.toResponse(dealership));
    }

    @GetMapping("/{id}/vehicles")
    public ResponseEntity<List<VehicleResponse>> findVehiclesByDealership(@PathVariable final UUID id) {
        findDealershipUseCase.findById(id);

        final var vehicles = findVehicleUseCase.findByDealershipId(id);
        final var response = vehicles.stream()
                .map(vehicleWebMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}