package br.com.accenture.dealership_management.infrastructure.adapter.out.integration;

import br.com.accenture.dealership_management.application.port.out.AddressLookupPort;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import br.com.accenture.dealership_management.domain.model.Address;
import br.com.accenture.dealership_management.domain.model.Cep;
import org.springframework.stereotype.Component;

@Component
public class ViaCepAdapter implements AddressLookupPort {

    private final ViaCepClient viaCepClient;

    public ViaCepAdapter(final ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    @Override
    public Address lookupByCep(final String cep) {
        try {
            final var response = viaCepClient.consultarCep(cep.replaceAll("\\D", ""));
            if (response.erro() != null && response.erro()) {
                throw new DomainBusinessException("CEP não encontrado no ViaCEP.");
            }
            return new Address(
                    new Cep(response.cep()),
                    response.logradouro(),
                    response.bairro(),
                    response.localidade(),
                    response.uf()
            );
        } catch (Exception e) {
            throw new DomainBusinessException("Erro ao consultar o CEP informado.");
        }
    }
}