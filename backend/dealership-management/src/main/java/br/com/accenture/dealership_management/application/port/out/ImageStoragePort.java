package br.com.accenture.dealership_management.application.port.out;

import java.io.InputStream;

public interface ImageStoragePort {
    String uploadImage(String fileName, InputStream inputStream, String contentType);
}

