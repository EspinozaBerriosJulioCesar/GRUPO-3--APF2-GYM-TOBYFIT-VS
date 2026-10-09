package com.gym.gym_tobifit.controllers.api;

import com.gym.gym_tobifit.dto.AuthRequest;
import com.gym.gym_tobifit.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** S09 diapositiva 11: autentica en la base de datos antes de emitir el token. */
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/auth")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername().trim(), request.getPassword()));
        String token = jwtUtil.generateToken(authentication.getName());
        // Se devuelve el token como texto, igual que en las diapositivas del profesor.
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
                .cacheControl(CacheControl.noStore()).body(token);
    }
}
