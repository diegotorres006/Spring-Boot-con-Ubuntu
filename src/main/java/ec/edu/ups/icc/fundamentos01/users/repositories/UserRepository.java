package ec.edu.ups.icc.Springboot01.users.repositories;

import ec.edu.ups.icc.Springboot01.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
