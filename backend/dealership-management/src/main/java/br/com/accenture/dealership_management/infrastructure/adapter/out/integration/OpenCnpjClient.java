package br.com.accenture.dealership_management.infrastructure.adapter.out.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "openCnpjClient", url = "https://api.opencnpj.org")
public interface OpenCnpjClient {

    @GetMapping("/{cnpj}?datasets=receita")
    OpenCnpjResponse consultarCnpj(@PathVariable("cnpj") String cnpj);
}