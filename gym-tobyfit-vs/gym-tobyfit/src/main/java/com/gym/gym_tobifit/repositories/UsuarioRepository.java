package com.gym.gym_tobifit.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.gym_tobifit.models.Usuario;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}
