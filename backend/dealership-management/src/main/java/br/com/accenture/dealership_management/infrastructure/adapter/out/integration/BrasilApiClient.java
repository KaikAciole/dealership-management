package br.com.accenture.dealership_management.infrastructure.adapter.out.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "brasilApiClient", url = "https://brasilapi.com.br/api/cnpj/v1")
public interface BrasilApiClient {
    @GetMapping("/{cnpj}")
    BrasilApiCnpjResponse consultarCnpj(@PathVariable("cnpj") String cnpj);
}