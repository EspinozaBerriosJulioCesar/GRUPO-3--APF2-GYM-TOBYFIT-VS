package com.gym.gym_tobifit.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gym.gym_tobifit.models.Usuario;
import com.gym.gym_tobifit.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        Usuario actual = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        if (usuario.getNombre() != null) {
            actual.setNombre(usuario.getNombre());
        }

        if (usuario.getApellido() != null) {
            actual.setApellido(usuario.getApellido());
        }

        if (usuario.getTelefono() != null) {
            actual.setTelefono(usuario.getTelefono());
        }

        if (usuario.getEdad() != null) {
            actual.setEdad(usuario.getEdad());
        }

        if (usuario.getCorreo() != null) {
            actual.setCorreo(usuario.getCorreo());
        }

        if (usuario.getRol() != null) {
            actual.setRol(usuario.getRol());
        }

        return usuarioRepository.save(actual);
    }

    public void restablecerContrasena(
            String correo,
            String nuevaContrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        usuario.setContrasena(
                passwordEncoder.encode(nuevaContrasena));

        usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
    }
}