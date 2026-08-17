package br.com.accenture.dealership_management.infrastructure.adapter.in.web;

import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.DealershipEntity;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.VehicleEntity;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.repository.SpringDataDealershipRepository;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.repository.SpringDataVehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class E2EApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataDealershipRepository dealershipRepository;

    @Autowired
    private SpringDataVehicleRepository vehicleRepository;

    private UUID dealershipId;
    private UUID vehicleId;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
        dealershipRepository.deleteAll();

        dealershipId = UUID.randomUUID();
        DealershipEntity dealership = DealershipEntity.builder()
                .id(dealershipId)
                .corporateName("Concessionaria Alfa")
                .cnpj("12345678000199")
                .cep("58400000")
                .street("Rua A")
                .number("100")
                .neighborhood("Centro")
                .city("Campina Grande")
                .state("PB")
                .foundationDate(LocalDate.of(2018, 1, 10))
                .isActive(true)
                .build();
        dealershipRepository.save(dealership);

        vehicleId = UUID.randomUUID();
        VehicleEntity vehicle = VehicleEntity.builder()
                .id(vehicleId)
                .brand("Fiat")
                .model("Pulse")
                .fuelType("FLEX")
                .color("Azul")
                .manufactureYear(2023)
                .chassis("9BWZZZ377VT004251")
                .price(BigDecimal.valueOf(100000))
                .externalColor("Azul")
                .dealershipId(dealershipId)
                .build();
        vehicleRepository.save(vehicle);
    }

    @Test
    void shouldListDealershipsAndVehicles() throws Exception {
        mockMvc.perform(get("/api/v1/dealerships").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(dealershipId.toString()))
                .andExpect(jsonPath("$.content[0].corporateName").value("Concessionaria Alfa"));

        mockMvc.perform(get("/api/v1/vehicles").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(vehicleId.toString()))
                .andExpect(jsonPath("$.content[0].brand").value("Fiat"));
    }

    @Test
    void shouldPreventDeletingDealershipWithLinkedVehicles() throws Exception {
        mockMvc.perform(delete("/api/v1/dealerships/{id}", dealershipId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("Não é possível excluir uma concessionária que possui veículos vinculados."));
    }

    @Test
    void shouldDeleteVehicleSuccessfully() throws Exception {
        mockMvc.perform(delete("/api/v1/vehicles/{id}", vehicleId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/vehicles/{id}", vehicleId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Veículo não encontrado."));
    }
}



