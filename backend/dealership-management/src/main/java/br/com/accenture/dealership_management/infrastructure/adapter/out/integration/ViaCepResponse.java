package br.com.accenture.dealership_management.infrastructure.adapter.out.integration;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        Boolean erro
) {}