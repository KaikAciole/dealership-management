package br.com.accenture.dealership_management.infrastructure.adapter.out.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenCnpjResponse(
        @JsonProperty("data_inicio_atividade") String dataInicioAtividade,
        @JsonProperty("situacao_cadastral") String situacaoCadastral
) {}