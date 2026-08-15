package br.com.accenture.dealership_management.application.port.out;

import java.time.LocalDate;

public interface CompanyInfoLookupPort {
    CompanyInfo lookupByCnpj(String cnpj);
    record CompanyInfo(LocalDate foundationDate, boolean isActive) {}
}