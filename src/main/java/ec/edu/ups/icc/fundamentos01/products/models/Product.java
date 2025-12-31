package ec.edu.ups.icc.fundamentos01.products.models;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;

    public Product() {}
    public Product(int id, String name, String description, double price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (price < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public static Product fromEntity(ProductEntity entity) {
        if (entity == null) return null;
        return new Product(entity.getId().intValue(), entity.getName(), entity.getDescription(), entity.getPrice());
    }

    public static Product fromDto(CreateProductDto dto) {
        return new Product(0, dto.name, dto.description, dto.price);
    }

    public ProductEntity toEntity() {
        ProductEntity entity = new ProductEntity();
        if (this.id > 0) entity.setId((long) this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        entity.setPrice(this.price);
        return entity;
    }

    public Product update(UpdateProductDto dto) {
        this.name = dto.name;
        this.description = dto.description;
        this.price = dto.price;
        return this;
    }

    public Product partialUpdate(PartialUpdateProductDto dto) {
        if (dto.name != null) this.name = dto.name;
        if (dto.description != null) this.description = dto.description;
        if (dto.price != null) this.price = dto.price;
        return this;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
}