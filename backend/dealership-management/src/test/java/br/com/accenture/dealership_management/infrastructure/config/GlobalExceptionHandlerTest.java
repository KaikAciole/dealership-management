package br.com.accenture.dealership_management.infrastructure.config;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleDomainBusinessException() {
        ProblemDetail detail = handler.handleDomainBusinessException(new DomainBusinessException("erro de regra"));

        assertEquals(422, detail.getStatus());
        assertEquals("erro de regra", detail.getDetail());
        assertEquals("Violação de Regra de Negócio", detail.getTitle());
        assertNotNull(detail.getProperties().get("timestamp"));
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        ProblemDetail detail = handler.handleIllegalArgumentException(new IllegalArgumentException("param invalido"));

        assertEquals(400, detail.getStatus());
        assertEquals("param invalido", detail.getDetail());
        assertEquals("Parâmetro Inválido", detail.getTitle());
        assertNotNull(detail.getProperties().get("timestamp"));
    }

    @Test
    void shouldHandleResponseStatusException() {
        ProblemDetail detail = handler.handleResponseStatusException(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "nao encontrado")
        );

        assertEquals(404, detail.getStatus());
        assertEquals("nao encontrado", detail.getDetail());
        assertNotNull(detail.getProperties().get("timestamp"));
    }

    @Test
    void shouldHandleUnexpectedException() {
        ProblemDetail detail = handler.handleUnexpectedException(new RuntimeException("boom"));

        assertEquals(500, detail.getStatus());
        assertEquals("Erro interno inesperado. Tente novamente mais tarde.", detail.getDetail());
        assertEquals("Erro Interno", detail.getTitle());
        assertNotNull(detail.getProperties().get("timestamp"));
    }
}

