package br.com.accenture.dealership_management.infrastructure.adapter.out.integration;

import br.com.accenture.dealership_management.application.port.out.CompanyInfoLookupPort;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OpenCnpjAdapter implements CompanyInfoLookupPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenCnpjAdapter.class);

    private final OpenCnpjClient openCnpjClient;

    public OpenCnpjAdapter(final OpenCnpjClient openCnpjClient) {
        this.openCnpjClient = openCnpjClient;
    }

    @Override
    public CompanyInfo lookupByCnpj(final String cnpj) {
        try {
            final String cnpjLimpo = cnpj.replaceAll("[^A-Za-z0-9]", "");
            final var response = openCnpjClient.consultarCnpj(cnpjLimpo);
            final boolean isActive = "ATIVA".equalsIgnoreCase(response.situacaoCadastral());
            final LocalDate foundationDate = LocalDate.parse(response.dataInicioAtividade());

            return new CompanyInfo(foundationDate, isActive);
        } catch (Exception e) {
            LOGGER.error("Erro ao consultar dados na OpenCNPJ para o CNPJ informado.", e);
            throw new DomainBusinessException("Erro ao consultar informações do CNPJ na OpenCNPJ.");
        }
    }
}