package br.com.accenture.dealership_management.domain.model;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import java.util.regex.Pattern;

public record Cnpj(String value) {
    private static final Pattern PATTERN = Pattern.compile("^\\d{14}$");

    public Cnpj {
        if (value == null || value.isBlank()) {
            throw new DomainBusinessException("O CNPJ não pode ser nulo ou vazio.");
        }

        String cleanValue = value.replaceAll("\\D", "");
        if (!PATTERN.matcher(cleanValue).matches()) {
            throw new DomainBusinessException("O formato do CNPJ é inválido. Deve conter 14 dígitos.");
        }
        value = cleanValue;
    }
}