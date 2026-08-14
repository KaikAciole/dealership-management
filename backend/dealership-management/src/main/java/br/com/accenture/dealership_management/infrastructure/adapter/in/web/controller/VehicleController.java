package br.com.accenture.dealership_management.infrastructure.adapter.in.web.controller;

import br.com.accenture.dealership_management.application.port.in.CreateVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.DeleteVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.FindVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.UpdateVehicleUseCase;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.VehicleRequest;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response.VehicleResponse;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper.VehicleWebMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final CreateVehicleUseCase createVehicleUseCase;
    private final FindVehicleUseCase findVehicleUseCase;
    private final UpdateVehicleUseCase updateVehicleUseCase;
    private final DeleteVehicleUseCase deleteVehicleUseCase;
    private final VehicleWebMapper mapper;

    public VehicleController(
            final CreateVehicleUseCase createVehicleUseCase,
            final FindVehicleUseCase findVehicleUseCase,
            final UpdateVehicleUseCase updateVehicleUseCase,
            final DeleteVehicleUseCase deleteVehicleUseCase,
            final VehicleWebMapper mapper
    ) {
        this.createVehicleUseCase = createVehicleUseCase;
        this.findVehicleUseCase = findVehicleUseCase;
        this.updateVehicleUseCase = updateVehicleUseCase;
        this.deleteVehicleUseCase = deleteVehicleUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@RequestBody final VehicleRequest request) {
        final var domain = mapper.toDomain(request);
        final var createdVehicle = createVehicleUseCase.create(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(createdVehicle));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> findById(@PathVariable final UUID id) {
        final var vehicle = findVehicleUseCase.findById(id);
        return ResponseEntity.ok(mapper.toResponse(vehicle));
    }

    @GetMapping
    public ResponseEntity<Page<VehicleResponse>> findAll(final Pageable pageable) {
        final var page = findVehicleUseCase.findAll(pageable);
        return ResponseEntity.ok(page.map(mapper::toResponse));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<VehicleResponse>> search(
            @RequestParam(required = false) final String brand,
            @RequestParam(required = false) final String color,
            @RequestParam(required = false) final Integer manufactureYear,
            final Pageable pageable
    ) {
        final var page = findVehicleUseCase.search(brand, color, manufactureYear, pageable);
        return ResponseEntity.ok(page.map(mapper::toResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> update(@PathVariable final UUID id, @RequestBody final VehicleRequest request) {
        final var domainData = mapper.toDomain(request);
        final var updatedVehicle = updateVehicleUseCase.update(id, domainData);
        return ResponseEntity.ok(mapper.toResponse(updatedVehicle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final UUID id) {
        deleteVehicleUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}