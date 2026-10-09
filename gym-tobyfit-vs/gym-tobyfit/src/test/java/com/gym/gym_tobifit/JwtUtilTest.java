package com.gym.gym_tobifit;

import com.gym.gym_tobifit.security.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {
    private static final String SECRET = "clave_de_prueba_unitaria_de_al_menos_64_bytes_tobyfit_2026_0123456789";
    private final JwtUtil jwtUtil = new JwtUtil(SECRET, 3600000);

    @Test void generaYValidaToken() {
        String token = jwtUtil.generateToken("admin@test.local");
        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.extractUsername(token)).isEqualTo("admin@test.local");
    }
    @Test void rechazaNuloVacioYMalformado() {
        assertThat(jwtUtil.validateToken(null)).isFalse();
        assertThat(jwtUtil.validateToken("")).isFalse();
        assertThat(jwtUtil.validateToken("abc.def.ghi")).isFalse();
    }
    @Test void rechazaFirmaConOtraClave() {
        JwtUtil other = new JwtUtil("otra_clave_de_prueba_totalmente_distinta_2026_98765", 3600000);
        assertThat(jwtUtil.validateToken(other.generateToken("admin@test.local"))).isFalse();
    }
    @Test void rechazaTokenSinExpiracion() {
        String token = Jwts.builder().setSubject("admin@test.local").setIssuedAt(new Date())
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256).compact();
        assertThat(jwtUtil.validateToken(token)).isFalse();
    }
    @Test void rechazaTokenSinUsuario() {
        String token = Jwts.builder().setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+60000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256).compact();
        assertThat(jwtUtil.validateToken(token)).isFalse();
    }
    @Test void rechazaAlgoritmoDistinto() {
        String token = Jwts.builder().setSubject("admin@test.local").setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+60000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS512).compact();
        assertThat(jwtUtil.validateToken(token)).isFalse();
    }
    @Test void rechazaClaveCorta() {
        assertThatThrownBy(() -> new JwtUtil("corta", 3600000)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void rechazaDuracionNoPositiva() {
        assertThatThrownBy(() -> new JwtUtil(SECRET, 0)).isInstanceOf(IllegalArgumentException.class);
    }
}
