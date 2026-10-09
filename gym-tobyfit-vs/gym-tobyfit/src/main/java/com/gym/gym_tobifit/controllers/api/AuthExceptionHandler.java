package com.gym.gym_tobifit.controllers.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> invalidCredentials(HttpServletRequest request) {
        return ResponseEntity.status(401).body(Map.of("status", 401,
                "error", "No autorizado", "message", "Credenciales inválidas", "path", request.getRequestURI()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<?> invalidRequest(HttpServletRequest request) {
        return ResponseEntity.badRequest().body(Map.of("status", 400, "error", "Solicitud inválida",
                "message", "Envía username y password válidos en un objeto JSON", "path", request.getRequestURI()));
    }
}
