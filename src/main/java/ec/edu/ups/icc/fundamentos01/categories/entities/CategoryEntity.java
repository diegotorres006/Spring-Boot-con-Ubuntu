package ec.edu.ups.icc.Springboot01.categories.entities;

import ec.edu.ups.icc.Springboot01.core.entities.BaseModel;
import ec.edu.ups.icc.Springboot01.products.entities.ProductEntity; 
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseModel {

    @Column(nullable = false, unique = true)
    private String name;
    
    private String description;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<ProductEntity> products = new HashSet<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
