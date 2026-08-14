package br.com.accenture.dealership_management.infrastructure.adapter.out.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record BrasilApiCnpjResponse(
        String cnpj,
        @JsonProperty("razao_social") String razaoSocial,
        @JsonProperty("data_inicio_atividade") LocalDate dataInicioAtividade,
        @JsonProperty("descricao_situacao_cadastral") String situacaoCadastral
) {}