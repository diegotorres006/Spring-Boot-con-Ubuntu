package ec.edu.ups.icc.Springboot01.users.repositories;

import ec.edu.ups.icc.Springboot01.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // Añade esta línea exactamente así:
    Optional<UserEntity> findByEmail(String email);
    
    boolean existsByEmail(String email);
}