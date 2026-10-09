package com.gym.gym_tobifit.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.gym_tobifit.models.Usuario;
import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // S07: consulta derivada sobre la propiedad correo de la entidad Usuario.
    Optional<Usuario> findByCorreoIgnoreCase(String correo);
}
