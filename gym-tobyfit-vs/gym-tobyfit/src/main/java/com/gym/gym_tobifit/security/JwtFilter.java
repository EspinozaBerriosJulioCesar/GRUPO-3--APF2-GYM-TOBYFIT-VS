
package com.gym.gym_tobifit.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT.
 * Valida el token y carga los permisos del usuario desde la BD.
 * S09 diapositiva 8.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;
    private final SecurityErrorHandler errorHandler;

    public JwtFilter(
            JwtUtil jwtUtil,
            MyUserDetailsService userDetailsService,
            SecurityErrorHandler errorHandler) {

        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.errorHandler = errorHandler;
    }

    /**
     * No aplicar el filtro JWT al endpoint de login.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "POST".equals(request.getMethod())
                && "/auth".equals(request.getServletPath());
    }

    /**
     * Valida el JWT y autentica al usuario.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            try {

                // Verificar el formato Bearer
                if (!authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
                    throw new BadCredentialsException(
                            "Se requiere Bearer");
                }

                // Extraer el token
                String token = authHeader.substring(7).trim();

                // Validar el token JWT
                if (!jwtUtil.validateToken(token)) {
                    throw new BadCredentialsException(
                            "Token inválido o vencido");
                }

                // Obtener el nombre del usuario
                String username = jwtUtil.extractUsername(token);

                // Cargar usuario y roles desde la BD
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                // Verificar el estado de la cuenta
                if (!userDetails.isEnabled()
                        || !userDetails.isAccountNonLocked()
                        || !userDetails.isAccountNonExpired()
                        || !userDetails.isCredentialsNonExpired()) {

                    throw new BadCredentialsException(
                            "Cuenta no disponible");
                }

                // Crear la autenticación
                var authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                // Agregar detalles de la petición
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                // Registrar la autenticación
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

            } catch (AuthenticationException
                     | JwtException
                     | IllegalArgumentException exception) {

                // Limpiar autenticación ante errores
                SecurityContextHolder.clearContext();

                // Responder con error de autenticación
                errorHandler.commence(
                        request,
                        response,
                        new BadCredentialsException("Token no válido"));

                return;
            }
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
