package br.com.accenture.dealership_management.infrastructure.adapter.out.storage;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class MinioStorageAdapterTest {

    @Test
    void shouldUploadImageAndBuildPublicUrlWithoutDuplicatedSlash() {
        S3Client s3Client = mock(S3Client.class);
        MinioStorageAdapter adapter = new MinioStorageAdapter(s3Client, "vehicle-images", "http://localhost:9000/");

        String url = adapter.uploadImage(
                "vehicles/a.jpg",
                new ByteArrayInputStream("img".getBytes(StandardCharsets.UTF_8)),
                "image/jpeg"
        );

        assertEquals("http://localhost:9000/vehicle-images/vehicles/a.jpg", url);
    }

    @Test
    void shouldUploadImageAndBuildPublicUrlWithBaseWithoutSlash() {
        S3Client s3Client = mock(S3Client.class);
        MinioStorageAdapter adapter = new MinioStorageAdapter(s3Client, "vehicle-images", "http://localhost:9000");

        String url = adapter.uploadImage(
                "vehicles/b.jpg",
                new ByteArrayInputStream("img".getBytes(StandardCharsets.UTF_8)),
                "image/jpeg"
        );

        assertEquals("http://localhost:9000/vehicle-images/vehicles/b.jpg", url);
    }

    @Test
    void shouldThrowBusinessExceptionWhenInputStreamFails() {
        S3Client s3Client = mock(S3Client.class);
        MinioStorageAdapter adapter = new MinioStorageAdapter(s3Client, "vehicle-images", "http://localhost:9000");

        InputStream brokenStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("boom");
            }
        };

        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> adapter.uploadImage("vehicles/c.jpg", brokenStream, "image/jpeg"));

        assertEquals("Falha ao fazer upload da imagem do veiculo.", ex.getMessage());
    }

    @Test
    void shouldThrowBusinessExceptionWhenS3Fails() {
        S3Client s3Client = mock(S3Client.class);
        MinioStorageAdapter adapter = new MinioStorageAdapter(s3Client, "vehicle-images", "http://localhost:9000");

        doThrow(S3Exception.builder().message("s3 failed").build()).when(s3Client)
                .putObject(any(software.amazon.awssdk.services.s3.model.PutObjectRequest.class), any(RequestBody.class));

        DomainBusinessException ex = assertThrows(DomainBusinessException.class,
                () -> adapter.uploadImage("vehicles/d.jpg", new ByteArrayInputStream(new byte[]{1}), "image/jpeg"));

        assertEquals("Falha ao fazer upload da imagem do veiculo.", ex.getMessage());
    }
}



