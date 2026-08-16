package br.com.accenture.dealership_management.application.port.out;

import br.com.accenture.dealership_management.domain.model.Address;

public interface AddressLookupPort {
    Address lookupByCep(String cep, String fallbackStreet, String fallbackNeighborhood, String number);
}