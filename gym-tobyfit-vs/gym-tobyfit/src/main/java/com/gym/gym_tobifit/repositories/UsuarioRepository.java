package com.gym.gym_tobifit.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym.gym_tobifit.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByCorreoIgnoreCase(String correo);
}