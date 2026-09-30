package com.gym.gym_tobifit.controllers.api;

import com.gym.gym_tobifit.models.Usuario;
import com.gym.gym_tobifit.services.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
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
