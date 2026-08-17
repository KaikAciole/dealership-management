package br.com.accenture.dealership_management.infrastructure.config;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class S3ConfigTest {

    @Test
    void shouldCreateS3Client() {
        S3Config config = new S3Config();

        S3Client client = config.s3Client(
                "http://localhost:9000",
                "us-east-1",
                "test-access",
                "test-secret"
        );

        assertNotNull(client);
        client.close();
    }
}

