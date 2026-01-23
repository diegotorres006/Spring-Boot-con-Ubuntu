package ec.edu.ups.icc.Springboot01.security.repositories;

import ec.edu.ups.icc.Springboot01.security.entities.RoleEntity;
import ec.edu.ups.icc.Springboot01.security.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(RoleName name);
    boolean existsByName(RoleName name);
}