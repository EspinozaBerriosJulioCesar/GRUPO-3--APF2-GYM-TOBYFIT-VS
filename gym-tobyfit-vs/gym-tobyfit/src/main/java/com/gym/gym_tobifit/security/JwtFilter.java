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

/** S09 diapositiva 8: valida el JWT y carga las autoridades desde la BD. */
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final MyUserDetailsService userDetailsService;
    private final SecurityErrorHandler errorHandler;

    public JwtFilter(JwtUtil jwtUtil, MyUserDetailsService userDetailsService,
                     SecurityErrorHandler errorHandler) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.errorHandler = errorHandler;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && "/auth".equals(request.getServletPath());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            try {
                if (!authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
                    throw new BadCredentialsException("Se requiere Bearer");
                }
                String token = authHeader.substring(7).trim();
                if (!jwtUtil.validateToken(token)) {
                    throw new BadCredentialsException("Token inválido o vencido");
                }
                String username = jwtUtil.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AuthenticationException | JwtException | IllegalArgumentException exception) {
                SecurityContextHolder.clearContext();
                errorHandler.commence(request, response, new BadCredentialsException("Token no válido"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
