package ec.edu.ups.icc.Springboot01.core.config;

import ec.edu.ups.icc.Springboot01.security.entities.RoleEntity;
import ec.edu.ups.icc.Springboot01.security.models.RoleName;
import ec.edu.ups.icc.Springboot01.security.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotFound(RoleName.ROLE_USER, "Usuario estándar");
        createRoleIfNotFound(RoleName.ROLE_ADMIN, "Administrador del sistema");
        createRoleIfNotFound(RoleName.ROLE_MODERATOR, "Moderador de contenido");
    }

    private void createRoleIfNotFound(RoleName name, String description) {
        if (!roleRepository.existsByName(name)) {
            RoleEntity role = new RoleEntity(name, description);
            roleRepository.save(role);
        }
    }
}