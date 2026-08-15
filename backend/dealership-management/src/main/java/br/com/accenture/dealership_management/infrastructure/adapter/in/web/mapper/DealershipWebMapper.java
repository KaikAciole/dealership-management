package br.com.accenture.dealership_management.infrastructure.adapter.in.web.mapper;

import br.com.accenture.dealership_management.domain.model.Address;
import br.com.accenture.dealership_management.domain.model.Cep;
import br.com.accenture.dealership_management.domain.model.Cnpj;
import br.com.accenture.dealership_management.domain.model.Dealership;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.request.DealershipRequest;
import br.com.accenture.dealership_management.infrastructure.adapter.in.web.dto.response.DealershipResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DealershipWebMapper {

    public Dealership toDomain(final DealershipRequest request) {
        final var addressRequest = request.address();
        final Cnpj cnpj = new Cnpj(request.cnpj());
        final Address address = new Address(
                new Cep(addressRequest.cep()),
                addressRequest.street(),
                addressRequest.neighborhood(),
                addressRequest.city(),
                addressRequest.state()
        );

        return new Dealership(
                UUID.randomUUID(),
                request.corporateName(),
                cnpj,
                address
        );
    }

    public DealershipResponse toResponse(final Dealership domain) {
        return new DealershipResponse(
                domain.getId(),
                domain.getCorporateName(),
                domain.getCnpj().value(),
                domain.getAddress().cep().value(),
                domain.getAddress().street(),
                domain.getAddress().neighborhood(),
                domain.getAddress().city(),
                domain.getAddress().state(),
                domain.getFoundationDate(),
                domain.isActive()
        );
    }
}