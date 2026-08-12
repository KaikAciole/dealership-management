package br.com.accenture.dealership_management.domain.model;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import java.util.regex.Pattern;

public record Cep(String value) {
    private static final Pattern PATTERN = Pattern.compile("^\\d{8}$");

    public Cep {
        if (value == null || value.isBlank()) {
            throw new DomainBusinessException("O CEP não pode ser nulo ou vazio.");
        }

        String cleanValue = value.replaceAll("\\D", "");
        if (!PATTERN.matcher(cleanValue).matches()) {
            throw new DomainBusinessException("O formato do CEP é inválido. Deve conter 8 dígitos.");
        }
        value = cleanValue;
    }
}