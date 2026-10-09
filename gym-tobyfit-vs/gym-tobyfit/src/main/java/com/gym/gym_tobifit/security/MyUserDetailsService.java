package com.gym.gym_tobifit.security;

import com.gym.gym_tobifit.models.Usuario;
import com.gym.gym_tobifit.repositories.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/** S09 diapositiva 7: consulta JPA y conversión del rol a una autoridad. */
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public MyUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales inválidas"));
        if (usuario.getRol() == null) {
            throw new UsernameNotFoundException("Usuario sin rol asignado");
        }
        return new User(usuario.getCorreo(), usuario.getContrasena(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())));
    }
}
