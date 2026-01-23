package ec.edu.ups.icc.Springboot01.security.services;

// IMPORT CORREGIDO SEGÚN TU CLASE:
import ec.edu.ups.icc.Springboot01.exception.domain.ConflictException;
import ec.edu.ups.icc.Springboot01.security.dtos.*;
import ec.edu.ups.icc.Springboot01.security.entities.RoleEntity;
import ec.edu.ups.icc.Springboot01.security.models.RoleName;
import ec.edu.ups.icc.Springboot01.security.repositories.RoleRepository;
import ec.edu.ups.icc.Springboot01.security.utils.JwtUtil;
import ec.edu.ups.icc.Springboot01.users.entities.UserEntity;
import ec.edu.ups.icc.Springboot01.users.repositories.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authManager, UserRepository userRepository, 
                       RoleRepository roleRepository, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        String jwt = jwtUtil.generateToken(auth);
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return new AuthResponseDto(jwt, userDetails.getId(), userDetails.getName(), userDetails.getEmail());
    }

    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        // Verifica si el usuario existe
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("El email ya está registrado");
        }
        
        UserEntity user = new UserEntity();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));

        RoleEntity userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
        
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        user = userRepository.save(user);
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String jwt = jwtUtil.generateToken(auth);

        return new AuthResponseDto(jwt, user.getId(), user.getName(), user.getEmail());
    }
}