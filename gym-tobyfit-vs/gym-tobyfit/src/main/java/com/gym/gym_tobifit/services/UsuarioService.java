
package com.gym.gym_tobifit.services;

import com.gym.gym_tobifit.models.Usuario;
import com.gym.gym_tobifit.repositories.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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

    public void eliminarUsuario(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
    }
}
