package br.com.accenture.dealership_management.infrastructure.adapter.out.storage;

import br.com.accenture.dealership_management.application.port.out.ImageStoragePort;
import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;

@Component
public class MinioStorageAdapter implements ImageStoragePort {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioStorageAdapter.class);

    private final S3Client s3Client;
    private final String bucket;
    private final String publicBaseUrl;

    public MinioStorageAdapter(
            final S3Client s3Client,
            @Value("${app.s3.bucket}") final String bucket,
            @Value("${app.s3.public-base-url}") final String publicBaseUrl
    ) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.publicBaseUrl = publicBaseUrl;
    }

    @Override
    public String uploadImage(final String fileName, final InputStream inputStream, final String contentType) {
        try {
            final byte[] data = inputStream.readAllBytes();

            final PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(data));
            return buildPublicUrl(fileName);
        } catch (IOException | S3Exception ex) {
            LOGGER.error("Falha ao fazer upload da imagem do veiculo para o bucket {}.", bucket, ex);
            throw new DomainBusinessException("Falha ao fazer upload da imagem do veiculo.");
        }
    }

    private String buildPublicUrl(final String fileName) {
        final String normalizedBase = publicBaseUrl.endsWith("/")
                ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1)
                : publicBaseUrl;
        return normalizedBase + "/" + bucket + "/" + fileName;
    }
}

