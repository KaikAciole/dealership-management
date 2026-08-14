package br.com.accenture.dealership_management.domain.model;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import java.time.LocalDate;
import java.util.UUID;

public class Dealership {
    private final UUID id;
    private String corporateName;
    private Cnpj cnpj;

    private Address address;
    private LocalDate foundationDate;
    private boolean isActive;

    public Dealership(
            final UUID id,
            final String corporateName,
            final Cnpj cnpj,
            final Address address
    ) {
        if (id == null) throw new DomainBusinessException("ID da concessionária é obrigatório.");
        if (corporateName == null || corporateName.isBlank()) throw new DomainBusinessException("Razão social é obrigatória.");
        if (cnpj == null) throw new DomainBusinessException("CNPJ é obrigatório.");
        if (address == null) throw new DomainBusinessException("Endereço é obrigatório.");

        this.id = id;
        this.corporateName = corporateName;
        this.cnpj = cnpj;
        this.address = address;
        this.isActive = true;
    }

    public void updateAddress(final Address newAddress) {
        if (newAddress == null) throw new DomainBusinessException("O novo endereço não pode ser nulo.");
        this.address = newAddress;
    }

    public void enrichWithOpenCnpjData(final LocalDate foundationDate, final boolean isActive) {
        this.foundationDate = foundationDate;
        this.isActive = isActive;
    }

    public void updateData(final String corporateName, final Cnpj cnpj, final Address address) {
        if (corporateName == null || corporateName.isBlank()) throw new DomainBusinessException("Razão social é obrigatória.");
        if (cnpj == null) throw new DomainBusinessException("CNPJ é obrigatório.");
        if (address == null) throw new DomainBusinessException("Endereço é obrigatório.");

        this.corporateName = corporateName;
        this.cnpj = cnpj;
        this.address = address;
    }

    public void toggleActive() {
        this.isActive = !this.isActive;
    }

    public UUID getId() { return id; }
    public String getCorporateName() { return corporateName; }
    public Cnpj getCnpj() { return cnpj; }
    public Address getAddress() { return address; }
    public LocalDate getFoundationDate() { return foundationDate; }
    public boolean isActive() { return isActive; }
}