package br.com.accenture.dealership_management.infrastructure.adapter.out.integration;

import br.com.accenture.dealership_management.application.port.out.CompanyInfoLookupPort;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.springframework.stereotype.Component;

@Component
public class BrasilApiAdapter implements CompanyInfoLookupPort {

    private final BrasilApiClient brasilApiClient;

    public BrasilApiAdapter(final BrasilApiClient brasilApiClient) {
        this.brasilApiClient = brasilApiClient;
    }

    @Override
    public CompanyInfo lookupByCnpj(final String cnpj) {
        try {
            final var response = brasilApiClient.consultarCnpj(cnpj.replaceAll("\\D", ""));
            final boolean isActive = "ATIVA".equalsIgnoreCase(response.situacaoCadastral());
            return new CompanyInfo(response.dataInicioAtividade(), isActive);
        } catch (Exception e) {
            throw new DomainBusinessException("Erro ao consultar informações do CNPJ.");
        }
    }
}