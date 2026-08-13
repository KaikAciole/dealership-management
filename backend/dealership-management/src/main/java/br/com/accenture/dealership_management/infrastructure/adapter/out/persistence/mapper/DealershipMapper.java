package br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.mapper;

import br.com.accenture.dealership_management.domain.model.Address;
import br.com.accenture.dealership_management.domain.model.Cep;
import br.com.accenture.dealership_management.domain.model.Cnpj;
import br.com.accenture.dealership_management.domain.model.Dealership;
import br.com.accenture.dealership_management.infrastructure.adapter.out.persistence.entity.DealershipEntity;
import org.springframework.stereotype.Component;

@Component
public class DealershipMapper {

    public DealershipEntity toEntity(final Dealership domain) {
        return DealershipEntity.builder()
                .id(domain.getId())
                .corporateName(domain.getCorporateName())
                .cnpj(domain.getCnpj().value())
                .cep(domain.getAddress().cep().value())
                .street(domain.getAddress().street())
                .neighborhood(domain.getAddress().neighborhood())
                .city(domain.getAddress().city())
                .state(domain.getAddress().state())
                .foundationDate(domain.getFoundationDate())
                .isActive(domain.isActive())
                .build();
    }

    public Dealership toDomain(final DealershipEntity entity) {
        final Cnpj cnpj = new Cnpj(entity.getCnpj());
        final Address address = new Address(
                new Cep(entity.getCep()),
                entity.getStreet(),
                entity.getNeighborhood(),
                entity.getCity(),
                entity.getState()
        );

        final Dealership dealership = new Dealership(
                entity.getId(),
                entity.getCorporateName(),
                cnpj,
                address
        );

        dealership.enrichWithOpenCnpjData(entity.getFoundationDate(), entity.isActive());
        return dealership;
    }
}