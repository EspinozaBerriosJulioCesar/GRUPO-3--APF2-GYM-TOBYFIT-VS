package com.gym.gym_tobifit.controllers.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gym.gym_tobifit.models.Usuario;
import com.gym.gym_tobifit.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioApiController {
    private final UsuarioService usuarioService;
    public UsuarioApiController(UsuarioService usuarioService) { this.usuarioService = usuarioService; }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() { return ResponseEntity.ok(usuarioService.findAll()); }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        if (usuario.getId() == null) usuario.setId(id);
        if (!id.equals(usuario.getId())) return ResponseEntity.badRequest().build();
        try { return ResponseEntity.ok(usuarioService.actualizarUsuario(usuario)); }
        catch (RuntimeException e) { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        try { usuarioService.eliminarUsuario(id); return ResponseEntity.ok().build(); }
        catch (RuntimeException e) { return ResponseEntity.notFound().build(); }
    }
}
