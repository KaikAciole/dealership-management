package br.com.accenture.dealership_management.application.port.in;

import java.util.UUID;

public interface DeleteDealershipUseCase {
    void delete(UUID id);
}