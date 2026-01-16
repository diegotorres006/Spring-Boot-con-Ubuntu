package ec.edu.ups.icc.fundamentos01.products.entities;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import java.util.Set;

public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;

    public Product() {}

    public Product(String name, Double price, String description) {
        this.validateBusinessRules(name, price, description);
        this.name = name;
        this.price = price;
        this.description = description;
    }

    private void validateBusinessRules(String name, Double price, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
    }

    
    public static Product fromDto(CreateProductDto dto) {
        return new Product(dto.name, dto.price, dto.description);
    }

    public static Product fromEntity(ProductEntity entity) {
        Product product = new Product(entity.getName(), entity.getPrice(), entity.getDescription());
        product.setId(entity.getId());
        return product;
    }


    public ProductEntity toEntity(UserEntity owner, Set<CategoryEntity> categories) {
        ProductEntity entity = new ProductEntity();
        
        if (this.id != null && this.id > 0) {
            entity.setId(this.id);
        }
        
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setDescription(this.description);
        
        entity.setOwner(owner);
        entity.setCategories(categories);
        
        return entity;
    }

    public void update(UpdateProductDto dto) {
        this.validateBusinessRules(dto.name, dto.price, dto.description);
        this.name = dto.name;
        this.price = dto.price;
        this.description = dto.description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}