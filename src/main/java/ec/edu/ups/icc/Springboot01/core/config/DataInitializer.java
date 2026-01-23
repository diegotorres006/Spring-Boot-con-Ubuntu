package ec.edu.ups.icc.Springboot01.core.config;

import ec.edu.ups.icc.Springboot01.security.entities.RoleEntity;
import ec.edu.ups.icc.Springboot01.security.models.RoleName;
import ec.edu.ups.icc.Springboot01.security.repositories.RoleRepository;
import ec.edu.ups.icc.Springboot01.users.entities.UserEntity;
import ec.edu.ups.icc.Springboot01.users.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Inicializar roles automáticamente desde el Enum
        initializeRoles();

        // 2. Crear usuario admin por defecto para pruebas
        createDefaultAdminUser();
    }

    private void initializeRoles() {
        logger.info("Iniciando la carga de roles del sistema...");

        for (RoleName roleName : RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                // Se usa el nombre y la descripción definidos en el Enum
                RoleEntity role = new RoleEntity(roleName, roleName.getDescription());
                roleRepository.save(role);
                logger.info("Rol insertado en BD: {}", roleName);
            }
        }
    }

    private void createDefaultAdminUser() {
        String adminEmail = "admin@ups.edu.ec";

        if (!userRepository.existsByEmail(adminEmail)) {
            logger.info("Creando usuario administrador por defecto...");

            UserEntity admin = new UserEntity();
            admin.setName("Administrador Sistema");
            admin.setEmail(adminEmail);
            // Hasheo de contraseña obligatorio con BCrypt
            admin.setPassword(passwordEncoder.encode("admin123"));

            // Buscar el rol ROLE_ADMIN en la BD
            RoleEntity adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error fatal: Rol ADMIN no existe"));

            Set<RoleEntity> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
            logger.info("Usuario administrador configurado correctamente: {}", adminEmail);
        }
    }
}