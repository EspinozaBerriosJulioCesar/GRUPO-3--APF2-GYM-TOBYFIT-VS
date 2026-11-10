package com.gym.gym_tobifit.controllers.auth;

import java.util.Map;

import com.gym.gym_tobifit.dto.LoginRequest;
import com.gym.gym_tobifit.dto.LoginResponse;
import com.gym.gym_tobifit.dto.RegistroRequest;
import com.gym.gym_tobifit.models.Usuario;
import com.gym.gym_tobifit.services.UsuarioService;
import com.gym.gym_tobifit.security.JwtUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UsuarioService usuarioService) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getContrasena()
                )
        );

        String token =
                jwtUtil.generateToken(request.getCorreo());

        return ResponseEntity.ok(
                new LoginResponse(token)
        );
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @RequestBody RegistroRequest request) {

        try {
            Usuario usuario = usuarioService.registrarUsuario(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "mensaje", "Usuario registrado correctamente",
                            "correo", usuario.getCorreo()
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}