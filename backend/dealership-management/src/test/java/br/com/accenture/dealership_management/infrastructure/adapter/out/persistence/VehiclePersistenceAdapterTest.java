package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence;

import br.com.accenture.dealership_management.domain.model.FuelType;
import br.com.accenture.dealership_management.domain.model.Vehicle;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.VehicleEntity;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.mapper.VehicleMapper;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.repository.SpringDataVehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehiclePersistenceAdapterTest {

    @Mock
    private SpringDataVehicleRepository repository;
    @Mock
    private VehicleMapper mapper;

    private VehiclePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new VehiclePersistenceAdapter(repository, mapper);
    }

    @Test
    void shouldSaveVehicle() {
        Vehicle domain = vehicle();
        VehicleEntity entity = new VehicleEntity();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Vehicle saved = adapter.save(domain);

        assertEquals(domain, saved);
    }

    @Test
    void shouldFindByIdAndFindAll() {
        UUID id = UUID.randomUUID();
        Vehicle domain = vehicle();
        VehicleEntity entity = new VehicleEntity();
        PageRequest pageable = PageRequest.of(0, 10);
        Page<VehicleEntity> page = new PageImpl<>(List.of(entity));

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDomain(entity)).thenReturn(domain);

        assertTrue(adapter.findById(id).isPresent());
        assertEquals(1, adapter.findAll(pageable).getTotalElements());
    }

    @Test
    void shouldDelegateExistsAndDeleteOperations() {
        UUID id = UUID.randomUUID();
        UUID dealershipId = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(true);
        when(repository.existsByDealershipId(dealershipId)).thenReturn(true);

        assertTrue(adapter.existsById(id));
        assertTrue(adapter.existsByDealershipId(dealershipId));

        adapter.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void shouldFindByDealershipAndSearch() {
        UUID dealershipId = UUID.randomUUID();
        Vehicle domain = vehicle();
        VehicleEntity entity = new VehicleEntity();
        PageRequest pageable = PageRequest.of(0, 10);

        when(repository.findAllByDealershipId(dealershipId)).thenReturn(List.of(entity));
        when(repository.findByFilters("fi", "azul", 2024, pageable)).thenReturn(new PageImpl<>(List.of(entity)));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Vehicle> list = adapter.findAllByDealershipId(dealershipId);
        Page<Vehicle> page = adapter.search("fi", "azul", 2024, pageable);

        assertEquals(1, list.size());
        assertEquals(1, page.getTotalElements());
    }

    private Vehicle vehicle() {
        return new Vehicle(UUID.randomUUID(), "Fiat", "Pulse", FuelType.FLEX, "Azul", UUID.randomUUID());
    }
}

