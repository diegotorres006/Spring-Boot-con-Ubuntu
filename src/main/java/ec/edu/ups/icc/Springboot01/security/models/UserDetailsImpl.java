package ec.edu.ups.icc.Springboot01.security.models;

import ec.edu.ups.icc.Springboot01.users.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String name;  // Añadido para solucionar error en AuthService
    private String email; // Añadido para solucionar error en JwtUtil
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String name, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(UserEntity user) {
        return new UserDetailsImpl(
            user.getId(),
            user.getName(), // Extraemos el nombre de la entidad
            user.getEmail(), // Extraemos el email de la entidad
            user.getPassword(),
            user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList())
        );
    }

    // Métodos personalizados que necesitan tus servicios
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    // Métodos obligatorios de la interfaz UserDetails
    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}