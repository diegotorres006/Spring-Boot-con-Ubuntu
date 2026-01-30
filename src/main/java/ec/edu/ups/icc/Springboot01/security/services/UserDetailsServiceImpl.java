package ec.edu.ups.icc.Springboot01.security.services;

import ec.edu.ups.icc.Springboot01.security.models.UserDetailsImpl;
import ec.edu.ups.icc.Springboot01.users.entities.UserEntity;
import ec.edu.ups.icc.Springboot01.users.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        return UserDetailsImpl.build(user);
    }
}