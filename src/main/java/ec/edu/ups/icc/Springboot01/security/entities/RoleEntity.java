package ec.edu.ups.icc.Springboot01.security.entities;

import ec.edu.ups.icc.Springboot01.core.entities.BaseModel;
import ec.edu.ups.icc.Springboot01.security.models.RoleName;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseModel {

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 50)
    private RoleName name;

    private String description;

    public RoleEntity() {}

    public RoleEntity(RoleName name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters y Setters
    public RoleName getName() { return name; }
    public void setName(RoleName name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}