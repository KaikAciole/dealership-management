package br.com.accenture.dealership_management.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CorsConfigTest {

    @Test
    void shouldConfigureCorsMappings() {
        CorsConfig config = new CorsConfig();
        CorsRegistry registry = new CorsRegistry();

        assertDoesNotThrow(() -> config.addCorsMappings(registry));
    }
}

