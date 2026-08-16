package br.com.accenture.dealership_management.infrastructure.adapter.in.web.controller;

import br.com.accenture.dealership_management.application.port.in.CreateVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.DeleteVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.FindVehicleUseCase;
import br.com.accenture.dealership_management.application.port.in.UploadVehicleImageUseCase;
import br.com.accenture.dealership_management.application.port.in.UpdateVehicleUseCase;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.VehicleRequest;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response.VehicleResponse;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper.VehicleWebMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final CreateVehicleUseCase createVehicleUseCase;
    private final FindVehicleUseCase findVehicleUseCase;
    private final UpdateVehicleUseCase updateVehicleUseCase;
    private final DeleteVehicleUseCase deleteVehicleUseCase;
    private final UploadVehicleImageUseCase uploadVehicleImageUseCase;
    private final VehicleWebMapper mapper;

    public VehicleController(
            final CreateVehicleUseCase createVehicleUseCase,
            final FindVehicleUseCase findVehicleUseCase,
            final UpdateVehicleUseCase updateVehicleUseCase,
            final DeleteVehicleUseCase deleteVehicleUseCase,
            final UploadVehicleImageUseCase uploadVehicleImageUseCase,
            final VehicleWebMapper mapper
    ) {
        this.createVehicleUseCase = createVehicleUseCase;
        this.findVehicleUseCase = findVehicleUseCase;
        this.updateVehicleUseCase = updateVehicleUseCase;
        this.deleteVehicleUseCase = deleteVehicleUseCase;
        this.uploadVehicleImageUseCase = uploadVehicleImageUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody final VehicleRequest request) {
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
    public ResponseEntity<Page<VehicleResponse>> findAll(@PageableDefault(sort = "brand", direction = org.springframework.data.domain.Sort.Direction.ASC) final Pageable pageable) {
        final var page = findVehicleUseCase.findAll(pageable);
        return ResponseEntity.ok(page.map(mapper::toResponse));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<VehicleResponse>> search(
            @RequestParam(required = false) final String brand,
            @RequestParam(required = false) final String color,
            @RequestParam(required = false) final Integer manufactureYear,
            @PageableDefault(sort = "brand", direction = org.springframework.data.domain.Sort.Direction.ASC) final Pageable pageable
    ) {
        final var page = findVehicleUseCase.search(brand, color, manufactureYear, pageable);
        return ResponseEntity.ok(page.map(mapper::toResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> update(@PathVariable final UUID id, @Valid @RequestBody final VehicleRequest request) {
        final var domainData = mapper.toDomain(request);
        final var updatedVehicle = updateVehicleUseCase.update(id, domainData);
        return ResponseEntity.ok(mapper.toResponse(updatedVehicle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final UUID id) {
        deleteVehicleUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<VehicleResponse> uploadImage(
            @PathVariable final UUID id,
            @RequestParam("file") final MultipartFile file
    ) {
        if (file.isEmpty()) {
            throw new DomainBusinessException("O arquivo da imagem nao pode estar vazio.");
        }

        try (var inputStream = file.getInputStream()) {
            final var updatedVehicle = uploadVehicleImageUseCase.uploadImage(
                    id,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    inputStream
            );
            return ResponseEntity.ok(mapper.toResponse(updatedVehicle));
        } catch (IOException ex) {
            throw new DomainBusinessException("Falha ao processar o arquivo da imagem.");
        }
    }
}