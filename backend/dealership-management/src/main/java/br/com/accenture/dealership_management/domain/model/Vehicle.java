package br.com.accenture.dealership_management.domain.model;

import br.com.accenture.dealership_management.domain.exception.DomainBusinessException;
import java.math.BigDecimal;
import java.util.UUID;

public class Vehicle {
    private final UUID id;
    private final String brand;
    private final String model;
    private final FuelType fuelType;
    private final String color;

    // Campos mutáveis através de regras de negócio
    private Integer manufactureYear;
    private String chassis;
    private BigDecimal price;
    private String externalColor;
    private String imageUrl;

    private UUID dealershipId;

    public Vehicle(
            final UUID id,
            final String brand,
            final String model,
            final FuelType fuelType,
            final String color,
            final UUID dealershipId
    ) {
        if (id == null) throw new DomainBusinessException("ID do veículo é obrigatório.");
        if (brand == null || brand.isBlank()) throw new DomainBusinessException("Marca é obrigatória.");
        if (model == null || model.isBlank()) throw new DomainBusinessException("Modelo é obrigatório.");
        if (fuelType == null) throw new DomainBusinessException("Tipo de combustível é obrigatório.");
        if (color == null || color.isBlank()) throw new DomainBusinessException("Cor é obrigatória.");
        if (dealershipId == null) throw new DomainBusinessException("O veículo deve estar associado a uma concessionária.");

        this.id = id;
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
        this.color = color;
        this.dealershipId = dealershipId;
    }

    public void updateOptionalData(
            final Integer manufactureYear,
            final String chassis,
            final BigDecimal price,
            final String externalColor
    ) {
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainBusinessException("O preço não pode ser negativo.");
        }
        this.manufactureYear = manufactureYear;
        this.chassis = chassis;
        this.price = price;
        this.externalColor = externalColor;
    }

    public void assignImage(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void transferToDealership(final UUID newDealershipId) {
        if (newDealershipId == null) throw new DomainBusinessException("A nova concessionária não pode ser nula.");
        this.dealershipId = newDealershipId;
    }

    public UUID getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public FuelType getFuelType() { return fuelType; }
    public String getColor() { return color; }
    public Integer getManufactureYear() { return manufactureYear; }
    public String getChassis() { return chassis; }
    public BigDecimal getPrice() { return price; }
    public String getExternalColor() { return externalColor; }
    public String getImageUrl() { return imageUrl; }
    public UUID getDealershipId() { return dealershipId; }
}